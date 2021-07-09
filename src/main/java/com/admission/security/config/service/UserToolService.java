package com.admission.security.config.service;

import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

import static com.admission.security.utils.JWTUtils.verify;

@Slf4j
@Service
public class UserToolService {
    //    @Autowired
//    SecurityContextService securityContextService;
    private final static int SUBSTRING_START_INDEX = 7;

    /**
     * 检查是否已登录
     */
    public boolean checkUserlogin(HttpServletRequest request) {
        //1、检查是否已登录
        /*String account = securityContextService.getLoginUserName();
        if (null == account) {
            throw new SessionAuthenticationException(ResultCode.USER_NOT_LOGIN.getMessage());
        }*/

        String authorization = request.getHeader("Authorization");
        if (Strings.isEmpty(authorization) || Strings.isBlank(authorization)) {
            log.info("请求token：{}", authorization);
            return false;
        }

        String token = authorization.substring(SUBSTRING_START_INDEX, authorization.length());
        try {
            DecodedJWT tokenInfo = verify(token);
            String account = tokenInfo.getClaim("username").asString();
        } catch (Exception e) {
            log.info("请求tokeng格式转换异常：{}", authorization);
            e.printStackTrace();
            return false;
        }
        return true;

        /*DecodedJWT tokenInfo = verify("eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJsb2dpbk5hbWUiOiJhZG1pbiIsImV4cCI6MTYyNTYyODQ0MiwidXNlcklkIjoiMSJ9.shRNDWemwxU-fUTGgcIqVSwOdTpRWbpkiPrGVqlXyp0");
        String userId = tokenInfo.getClaim("userId").asString();
        String loginName = tokenInfo.getClaim("loginName").asString();*/
//        return ResultTool.success();
    }

    /**
     * 获取当前登录用户账户名
     */
    public String getLoginUser(HttpServletRequest request) {
        /*String account = securityContextService.getLoginUserName();
        if (null == account) {
            throw new SessionAuthenticationException(ResultCode.USER_NOT_LOGIN.getMessage());
        }
        return account;*/

        String authorization = request.getHeader("Authorization");
        String token = authorization.substring(SUBSTRING_START_INDEX, authorization.length());
        DecodedJWT tokenInfo = verify(token);
        String account = tokenInfo.getClaim("username").asString();
        return account;
    }
}
