package com.admission.security.common.utils;


import com.admission.security.common.entity.JsonResult;
import com.admission.security.common.enums.ResultCode;

/**
 * @Author: Hutengfei
 * @Description:
 * @Date Create in 2019/7/22 19:52
 */
public class ResultTool {
    public static JsonResult success() {
        return new JsonResult(true);
    }

    public static <T> JsonResult<T> success(T data) {
        return new JsonResult(true, data);
    }

    public static JsonResult fail() {
        return new JsonResult(false);
    }

    public static JsonResult fail(ResultCode resultEnum) {
        return new JsonResult(false, resultEnum);
    }

    public static JsonResult failByMsg(String msg) {
        return new JsonResult(false, msg);
    }

    public static JsonResult fail(Integer code, String msg) {
        return new JsonResult(false, code, msg, "");
    }
}
