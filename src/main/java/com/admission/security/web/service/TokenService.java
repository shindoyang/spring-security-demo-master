package com.admission.security.web.service;

import com.admission.security.constant.Constants;
import com.admission.security.entity.SysUser;
import com.admission.security.entity.model.LoginUser;
import com.admission.security.redis.RedisCache;
import com.admission.security.service.SysUserService;
import com.admission.security.utils.IdUtils;
import com.admission.security.utils.IpUtils;
import com.admission.security.utils.ServletUtils;
import com.admission.security.utils.StringUtils;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import eu.bitwalker.useragentutils.UserAgent;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class TokenService {
    // 令牌自定义标识
    @Value("${token.header}")
    private String header;

    // 令牌秘钥
    @Value("${token.secret}")
    private String secret;

    // 令牌有效期（默认30分钟）
    @Value("${token.expireTime}")
    private int expireTime;

    protected static final long MILLIS_SECOND = 1000;

    protected static final long MILLIS_MINUTE = 60 * MILLIS_SECOND;

    private static final Long MILLIS_MINUTE_TEN = 20 * 60 * 1000L;

    @Autowired
    private RedisCache redisCache;
    @Autowired
    private SysUserService userService;

    /**
     * 获取用户身份信息
     *
     * @return 用户信息
     */
    public LoginUser getLoginUser(HttpServletRequest request) {
        // 获取请求携带的令牌
        String token = getToken(request);
        token = token.substring(7, token.length());
        if (StringUtils.isNotEmpty(token)) {
            Claims claims = parseToken(token);
            // 解析对应的权限以及用户信息
            String uuid = (String) claims.get(Constants.LOGIN_USER_KEY);
            String userKey = getTokenKey(uuid);
            LoginUser user = redisCache.getCacheObject(userKey);
            return user;
        } else {
            return getLoginUserForApi(request);
        }
    }

    private LoginUser getLoginUserForApi(HttpServletRequest request) {
        LoginUser loginUser = null;
        String appId = request.getHeader("appId");
        String timestamp = request.getHeader("timestamp");
        String sign = request.getHeader("sign");

        if (StringUtils.isNotBlank(appId) && StringUtils.isNotBlank(timestamp) && StringUtils.isNotBlank(sign)) {
            String etprStr = redisCache.getCacheObject(String.format(Constants.ETPRINFO_REDIS_KEY, appId));
            JSONObject etprJson = JSONObject.parseObject(etprStr);
            if (null != etprJson && etprJson.getInteger("status") == 0) {
                String appSecret = etprJson.getString("appSecret");

                boolean isValidate = validateIpWhite(etprJson.getString("ipWhite"));
                if (isValidate) {
                    isValidate = validateApiReq(appId, appSecret, timestamp, sign);
                }
                if (isValidate) {
                    String userKey = getTokenKey(appId);
                    loginUser = redisCache.getCacheObject(userKey);
                    if (null == loginUser) { // 缓存中不存在则查询该企业下的用户，然后保存进缓存
                        QueryWrapper<SysUser> qw = new QueryWrapper<>();
                        qw.eq("etpr_info_id", etprJson.getString("id"));
                        qw.eq("user_type", "01"); // 01-企业管理员
                        qw.last("LIMIT 1");
                        SysUser sysUser = userService.selectByName("qw");

                        if (sysUser != null) {
                            loginUser = new LoginUser();
                            loginUser.setExpireTime(System.currentTimeMillis() + expireTime * MILLIS_MINUTE * 60 * 24);
                            loginUser.setUser(sysUser);
                            // 放置进缓存
                            userKey = getTokenKey(appId);
                            redisCache.setCacheObject(userKey, loginUser, expireTime * 60 * 24, TimeUnit.MINUTES);
                        }
                    }
                }
            }
        }
        return loginUser;
    }

    private boolean validateApiReq(String appId, String appSecret, String timestamp, String sign) {
        boolean isValidate = false;

        // 默认五分钟内有效
        if (Math.abs(System.currentTimeMillis() - Long.parseLong(timestamp)) > 300000000) {
            log.info("账号：{}，超过5分钟验证，鉴权失败！", appId);
            return isValidate;
        }

        if (!sign.equals(DigestUtils.md5Hex(DigestUtils.sha256Hex(appId + appSecret + timestamp)))) {
            log.info("账号：{},签名验证不正确，鉴权失败！ {}", appId);
            return isValidate;
        }

        isValidate = true;
        return isValidate;
    }

    private boolean validateIpWhite(String ipWhite) {
        boolean isWhite = false;
        if (StringUtils.isNotBlank(ipWhite)) {
            String ip = IpUtils.getIpAddr(ServletUtils.getRequest());
            if (StringUtils.contains(ipWhite, ip)) {
                isWhite = true;
            }
        } else {
            isWhite = true;
        }

        return isWhite;
    }

    /**
     * 更新密码后，清除token 清除指令牌
     */
    public void clearTokenCache(HttpServletRequest request) {
        if (request == null) {
            return;
        }
        LoginUser loginUser = getLoginUser(request);
        if (loginUser == null) {
            return;
        }
        delLoginUser(loginUser.getToken());
    }

    /**
     * 设置用户代理信息
     *
     * @param loginUser 登录信息
     */
    public void setUserAgent(LoginUser loginUser) {
        UserAgent userAgent = UserAgent.parseUserAgentString(ServletUtils.getRequest().getHeader("User-Agent"));
        String ip = IpUtils.getIpAddr(ServletUtils.getRequest());
        loginUser.setIpaddr(ip);
        loginUser.setBrowser(userAgent.getBrowser().getName());
        loginUser.setOs(userAgent.getOperatingSystem().getName());
    }

    /**
     * 刷新令牌有效期
     *
     * @param loginUser 登录信息
     */
    public void refreshToken(LoginUser loginUser) {
        loginUser.setLoginTime(System.currentTimeMillis());
        loginUser.setExpireTime(loginUser.getLoginTime() + expireTime * MILLIS_MINUTE);
        // 根据uuid将loginUser缓存
        String userKey = getTokenKey(loginUser.getToken());
        redisCache.setCacheObject(userKey, loginUser, expireTime, TimeUnit.MINUTES);
    }

    /**
     * 验证令牌有效期，相差不足20分钟，自动刷新缓存
     */
    public void verifyToken(LoginUser loginUser) {
        long expireTime = loginUser.getExpireTime();
        long currentTime = System.currentTimeMillis();
        if ((expireTime - currentTime) <= MILLIS_MINUTE_TEN) {
            refreshToken(loginUser);
        }
    }

    /**
     * 创建令牌
     *
     * @param loginUser 用户信息
     * @return 令牌
     */
    public String createToken(LoginUser loginUser) {
        String token = IdUtils.fastUUID();
        loginUser.setToken(token);
        setUserAgent(loginUser);
        refreshToken(loginUser);

        Map<String, Object> claims = new HashMap<>();
        claims.put(Constants.LOGIN_USER_KEY, token);
        return createJwtToken(claims);
    }

    /**
     * 从令牌中获取数据声明
     *
     * @param token 令牌
     * @return 数据声明
     */
    private Claims parseToken(String token) {
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
    }

    /**
     * 获取请求token
     *
     * @param request
     * @return token
     */
    private String getToken(HttpServletRequest request) {
        String token = request.getHeader(header);
        if (StringUtils.isNotEmpty(token) && token.startsWith(Constants.TOKEN_PREFIX)) {
            token = token.replace(Constants.TOKEN_PREFIX, "");
        }
        return token;
    }

    private String getTokenKey(String uuid) {
        return Constants.LOGIN_TOKEN_KEY + uuid;
    }

    /**
     * 从数据声明生成令牌
     *
     * @param claims 数据声明
     * @return 令牌
     */
    private String createJwtToken(Map<String, Object> claims) {
        String token = Jwts.builder().setClaims(claims).signWith(SignatureAlgorithm.HS512, secret).compact();
        return token;
    }

    /**
     * 删除用户身份信息
     */
    public void delLoginUser(String token) {
        if (StringUtils.isNotEmpty(token)) {
            String userKey = getTokenKey(token);
            redisCache.deleteObject(userKey);
        }
    }

    /**
     * 设置用户身份信息
     */
    public void setLoginUser(LoginUser loginUser) {
        if (StringUtils.isNotNull(loginUser) && StringUtils.isNotEmpty(loginUser.getToken())) {
            refreshToken(loginUser);
        }
    }
}
