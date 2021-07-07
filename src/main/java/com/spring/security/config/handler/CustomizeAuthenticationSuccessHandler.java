package com.spring.security.config.handler;
/*

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.spring.security.common.entity.JsonResult;
import com.spring.security.common.utils.ResultTool;
import com.spring.security.entity.SysUser;
import com.spring.security.service.SysPermissionService;
import com.spring.security.service.SysUserService;
import com.spring.security.utils.JWTUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

*/
/**
 * @Author: Hutengfei
 * @Description: 登录成功处理逻辑
 * @Date Create in 2019/9/3 15:52
 *//*

@Component
public class CustomizeAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    @Autowired
    SysUserService sysUserService;
    @Autowired
    SysPermissionService sysPermissionService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {
        //更新用户表上次登录时间、更新人、更新时间等字段
        User userDetails = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        SysUser sysUser = sysUserService.selectByName(userDetails.getUsername());
        sysUser.setLastLoginTime(new Date());
        sysUser.setUpdateTime(new Date());
        sysUser.setUpdateUser(sysUser.getId());
        sysUserService.update(sysUser);

        //此处还可以进行一些处理，比如登录成功之后可能需要返回给前台当前用户有哪些菜单权限，
        //进而前台动态的控制菜单的显示等，具体根据自己的业务需求进行扩展

        //返回json数据
        */
/*JsonResult result = ResultTool.success();
        Map userMap = new HashMap<>();
        userMap.put("account", sysUser.getAccount());
        userMap.put("username", sysUser.getUserName());

        result.setData(JSONObject.toJSON(userMap));
        httpServletResponse.setContentType("text/json;charset=utf-8");
        httpServletResponse.getWriter().write(JSON.toJSONString(result));*//*


//===================
        Map<String, String> map = new HashMap<>();
        map.put("username", sysUser.getAccount());
//        map.put("roles", authentication.getAuthorities());
        //用这个工具类生成token
        String token = JWTUtils.generateTokenExpireInMinutes(map, 24 * 60);
        //将token加到请求头里面
        httpServletResponse.addHeader("Authorization", "Bearer " + token);
        try {
            httpServletResponse.setContentType("application/json;charset=utf-8");
            httpServletResponse.setStatus(HttpServletResponse.SC_OK);
            PrintWriter out = httpServletResponse.getWriter();
            Map back = new HashMap<>();
            back.put("account", sysUser.getAccount());
            back.put("username", sysUser.getUserName());
            back.put("token", token);
            JsonResult result = ResultTool.success();
            result.setData(JSONObject.toJSON(back));
//            out.write(new ObjectMapper().writeValueAsString(resultMap));
            out.write(JSON.toJSONString(result));
            out.flush();
            out.close();
        } catch (Exception outEx) {
            outEx.printStackTrace();
        }
    }
}
*/
