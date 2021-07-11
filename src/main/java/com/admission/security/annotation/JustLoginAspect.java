package com.admission.security.annotation;

import com.admission.security.common.enums.ResultCode;
import com.admission.security.common.utils.ResultTool;
import com.alibaba.fastjson.JSONObject;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.Objects;
import java.util.Optional;

import static com.admission.security.utils.JWTUtils.verify;

@Slf4j
@Aspect
@Component
public class JustLoginAspect {
    private final static int SUBSTRING_START_INDEX = 7;

    @Before("@annotation(justLogin)")
    public void checkLogin(JustLogin justLogin) throws Exception {
        HttpServletRequest request = null;
        String token = null;
        PrintWriter writer = null;
        request = currentRequest();
        HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
        writer = response.getWriter();
        if (Objects.isNull(request)) {
            log.info("without request, skip");
            return;
        }

        String requestToken = null;
        Enumeration<String> headerNames1 = request.getHeaderNames();

        while (headerNames1.hasMoreElements()) {
            String key = (String) headerNames1.nextElement();
            // 排除Cookie字段
            if (key.equalsIgnoreCase("Cookie")) {
                continue;
            }
            if (key.equalsIgnoreCase("Authorization")) {
                requestToken = request.getHeader(key);
            }
        }

        if (null == requestToken) {
            writer.write(JSONObject.toJSONString(ResultTool.fail(ResultCode.USER_NOT_LOGIN)));
        }

        token = requestToken.substring(SUBSTRING_START_INDEX, requestToken.length());
        DecodedJWT tokenInfo = verify(token);
        String account = tokenInfo.getClaim("username").asString();
        //将RequestAttributes对象设置为子线程共享
            /*ServletRequestAttributes sra = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            sra.setAttribute("curUser", account);
            RequestContextHolder.setRequestAttributes(sra, true);*/
    }

    /*@After("@annotation(com.admission.security.annotation.JustLogin)")
    public JsonResult serviceExceptionHandler(ProceedingJoinPoint proceedingJoinPoint) {
        JsonResult returnMsg;
        try {
            returnMsg = (JsonResult) proceedingJoinPoint.proceed();
        } catch (Throwable throwable) {
            log.error("ServiceExcepHandler serviceExcepHandler failed", throwable);
            //单独处理缺少参数异常
            if (throwable instanceof JWTDecodeException) {
                returnMsg = ResultTool.fail(ResultCode.USER_NOT_LOGIN);
            } else if (throwable instanceof TokenExpiredException) {//其他正常返回
                returnMsg = ResultTool.fail(ResultCode.USER_TOKEN_EXPIRE_ERROR);
            } else {
                returnMsg = ResultTool.fail();
            }
        }
        return returnMsg;
    }*/

    /**
     * Return request current thread bound or null if none bound.
     *
     * @return Current request or null
     */
    private HttpServletRequest currentRequest() {
        // Use getRequestAttributes because of its return null if none bound
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        return Optional.ofNullable(servletRequestAttributes).map(ServletRequestAttributes::getRequest).orElse(null);
    }
}
