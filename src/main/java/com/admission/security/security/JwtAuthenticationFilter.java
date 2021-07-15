package com.admission.security.security;

import com.admission.security.config.handler.CustomizeAuthenticationEntryPoint;
import com.admission.security.utils.SecurityUtils;
import com.admission.security.utils.SpringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 登录认证检查过滤器
 *
 * @author Louis
 * @date Jun 29, 2019
 */
@Slf4j
public class JwtAuthenticationFilter extends BasicAuthenticationFilter {

    @Autowired
    public JwtAuthenticationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        String requestURI = request.getRequestURI();
        log.info("当前用户：{}，请求接口：{}", SecurityUtils.getUsername(), requestURI);
        // 获取token, 并检查登录状态
        Object authentication = SecurityUtils.checkAuthentication(request);
        if (null != authentication) {
            chain.doFilter(request, response);
        } else {
            CustomizeAuthenticationEntryPoint customizeAuthenticationEntryPoint = (CustomizeAuthenticationEntryPoint) SpringUtils.getBean("customizeAuthenticationEntryPoint");
            customizeAuthenticationEntryPoint.commence(request, response, new AuthenticationServiceException("token 异常"));
        }
    }

}