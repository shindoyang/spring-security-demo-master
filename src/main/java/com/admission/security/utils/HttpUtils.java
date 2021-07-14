package com.admission.security.utils;

import com.admission.security.common.entity.JsonResult;
import com.admission.security.common.utils.ResultTool;
import com.alibaba.fastjson.JSONObject;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * HTTP工具类
 *
 * @author Louis
 * @date Jun 29, 2019
 */
public class HttpUtils {

    /**
     * 获取HttpServletRequest对象
     *
     * @return
     */
    public static HttpServletRequest getHttpServletRequest() {
        return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
    }

    /**
     * 输出信息到浏览器
     *
     * @throws IOException
     */
    public static void write(HttpServletResponse response, Object data) throws IOException {
        response.setContentType("application/json; charset=utf-8");
        JsonResult result = ResultTool.success(data);
        String json = JSONObject.toJSONString(result);
        response.getWriter().print(json);
        response.getWriter().flush();
        response.getWriter().close();
    }

}
