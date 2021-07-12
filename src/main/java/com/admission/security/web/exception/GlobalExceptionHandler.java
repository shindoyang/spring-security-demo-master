package com.admission.security.web.exception;

import com.admission.security.common.entity.JsonResult;
import com.admission.security.common.utils.ResultTool;
import com.admission.security.constant.HttpStatus;
import com.admission.security.exception.BaseException;
import com.admission.security.exception.CustomException;
import com.admission.security.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

/**
 * 全局异常处理器
 *
 * @author ruoyi
 */
@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * 基础异常
     */
    @ExceptionHandler(BaseException.class)
    public JsonResult baseException(BaseException e) {
        return ResultTool.failByMsg(e.getMessage());
    }

    /**
     * 业务异常
     */
    @ExceptionHandler(CustomException.class)
    public JsonResult businessException(CustomException e) {
        if (StringUtils.isNull(e.getCode())) {
            return ResultTool.failByMsg(e.getMessage());
        }
        return ResultTool.fail(e.getCode(), e.getMessage());
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public JsonResult handlerNoFoundException(Exception e) {
        log.error(e.getMessage(), e);
        return ResultTool.fail(HttpStatus.NOT_FOUND, "路径不存在，请检查路径是否正确");
    }

    @ExceptionHandler(AccessDeniedException.class)
    public JsonResult handleAuthorizationException(AccessDeniedException e) {
        log.error(e.getMessage());
        return ResultTool.fail(HttpStatus.FORBIDDEN, "没有权限，请联系管理员授权");
    }

    @ExceptionHandler(AccountExpiredException.class)
    public JsonResult handleAccountExpiredException(AccountExpiredException e) {
        log.error(e.getMessage(), e);
        return ResultTool.failByMsg(e.getMessage());
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public JsonResult handleUsernameNotFoundException(UsernameNotFoundException e) {
        log.error(e.getMessage(), e);
        return ResultTool.failByMsg(e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public JsonResult handleException(Exception e) {
        log.error(e.getMessage(), e);
        return ResultTool.failByMsg(e.getMessage());
    }

    /**
     * 自定义验证异常
     */
    @ExceptionHandler(BindException.class)
    public JsonResult validatedBindException(BindException e) {
        log.error(e.getMessage(), e);
        String message = e.getAllErrors().get(0).getDefaultMessage();
        return ResultTool.failByMsg(message);
    }

    /**
     * 自定义验证异常
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Object validExceptionHandler(MethodArgumentNotValidException e) {
        log.error(e.getMessage(), e);
        String message = e.getBindingResult().getFieldError().getDefaultMessage();
        return ResultTool.failByMsg(message);
    }
}
