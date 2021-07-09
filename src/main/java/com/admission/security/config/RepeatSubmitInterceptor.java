package com.admission.security.config;

/*import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

@Component
public abstract class RepeatSubmitInterceptor extends HandlerInterceptorAdapter {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            Method method = handlerMethod.getMethod();
            RepeatSubmit annotation = method.getAnnotation(RepeatSubmit.class);
            if (annotation != null) {
                if (this.isRepeatSubmit(request)) {
                    JsonResult jsonResult = new JsonResult(true, "不允许重复提交，请稍后再试");
                    ServletUtils.renderString(response, JSONObject.toJSONString(jsonResult));
                    return false;
                }
            }
            return true;
        } else {
            return super.preHandle(request, response, handler);
        }
    }

    *//**
 * 验证是否重复提交由子类实现具体的防重复提交的规则
 *
 * @return
 * @throws Exception
 *//*
    public abstract boolean isRepeatSubmit(HttpServletRequest request);
}*/
