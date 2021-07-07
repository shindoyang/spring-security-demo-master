package com.spring.security.config.filter;
/*
import com.fasterxml.jackson.databind.ObjectMapper;
import com.spring.security.entity.SysUser;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

public class JwtLoginFilter extends UsernamePasswordAuthenticationFilter {

    private AuthenticationManager authenticationManager;
    //将生成私钥和公钥的配置类引入要里面
    //private RsaKeyProperties prop;

    //带参构造方法
    *//*public JwtLoginFilter(AuthenticationManager authenticationManager, RsaKeyProperties prop) {
        this.authenticationManager = authenticationManager;
        this.prop = prop;
    }*//*

    //这个方法是源码里面的，我们只需要重写这个方法里面的内容就可以了，这个是认证的方法

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try {
            //因为前端是ajax发送 的信息，所以接受的时候，直接从request里面接受
            SysUser sysUser = new ObjectMapper().readValue(request.getInputStream(), SysUser.class);
            //将提取出来的用户名和密码封装为框架人家的对象
            UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(sysUser.getAccount(), sysUser.getPassword());
            return authenticationManager.authenticate(authRequest);
        } catch (Exception e) {
            try {
                //用户名和密码错误怎么办，要告诉用户
                response.setContentType("application/json;charset=utf-8");
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                PrintWriter out = response.getWriter();
                Map resultMap = new HashMap();
                resultMap.put("code", HttpServletResponse.SC_UNAUTHORIZED);
                resultMap.put("msg", "用户名或密码错误！");
                out.write(new ObjectMapper().writeValueAsString(resultMap));
                out.flush();
                out.close();
            } catch (Exception outEx) {
                outEx.printStackTrace();
            }
            throw new RuntimeException(e);
        }
    }

}*/


