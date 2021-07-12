package com.admission.security.web.service;


import com.admission.security.entity.model.LoginUser;
import com.admission.security.exception.CustomException;
import com.admission.security.exception.user.UserPasswordNotMatchException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class SysLoginService {
    protected final Logger logger = LoggerFactory.getLogger(SysLoginService.class);

    @Autowired
    private TokenService tokenService;

    @Resource
    private AuthenticationManager authenticationManager;

    /**
     * 登录验证
     *
     * @param username 用户名
     * @param password 密码
     * @param code     验证码
     * @param uuid     唯一标识
     * @return 结果
     */
    public String login(String username, String password, String code, String uuid) {
        // 用户验证
        Authentication authentication = null;
        try {
            // 该方法会去调用UserDetailsServiceImpl.loadUserByUsername
            authentication = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (Exception e) {
            logger.error(e.getMessage());
            if (e instanceof BadCredentialsException) {
                throw new UserPasswordNotMatchException();
            } else if (e instanceof UsernameNotFoundException) {
                throw new UsernameNotFoundException("登录用户不存在");
            } else {
                throw new CustomException(e.getMessage());
            }
        }

        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        /*if ("客户".equals(loginUser.getUser().getCspUserType())) {
            AsyncManager.me().execute(AsyncFactory.recordOper(loginUser.getUser().getUserName(), loginUser.getUser().getEtprInfoId(), 0, "登录用户：{" + loginUser.getUser().getUserName() + "} 登录成功."));
        }*/
        // 生成token
        return tokenService.createToken(loginUser);
    }
}
