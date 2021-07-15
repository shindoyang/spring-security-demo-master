package com.admission.security.config.service;

import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

import static com.admission.security.utils.JWTUtils.verify;

@Slf4j
@Service
public class UserToolService {
    private final static int SUBSTRING_START_INDEX = 7;

    /**
     * 获取当前登录用户账户名
     */
    public String getLoginUser(HttpServletRequest request) {
        String authorization = request.getHeader("Authorization");
        String token = authorization.substring(SUBSTRING_START_INDEX, authorization.length());
        DecodedJWT tokenInfo = verify(token);
        String account = tokenInfo.getClaim("username").asString();
        return account;
    }
}
