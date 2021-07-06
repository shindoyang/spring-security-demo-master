package com.spring.security.config.service;

import com.spring.security.common.entity.JsonResult;
import com.spring.security.common.enums.ResultCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.authentication.session.SessionAuthenticationException;
import org.springframework.stereotype.Service;

@Service
public class UserToolService {
    @Autowired
    SecurityContextService securityContextService;

    public JsonResult checkUserlogin() {
        //1、检查是否已登录
        String account = securityContextService.getLoginUserName();
        if (null == account) {
            throw new SessionAuthenticationException(ResultCode.USER_NOT_LOGIN.getMessage());
        }
        JsonResult result = new JsonResult();
        result.setData(account);
        return result;
    }
}
