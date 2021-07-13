package com.admission.security.common.enums;

/**
 * @Author: Hutengfei
 * @Description: 返回码定义
 * 规定:
 * #1表示成功
 * #1001～1999 区间表示参数错误
 * #2001～2999 区间表示用户错误
 * #3001～3999 区间表示接口异常
 * @Date Create in 2019/7/22 19:28
 */
public enum ResultCode {
    /* 成功 */
    SUCCESS(200, "成功"),

    /* 默认失败 */
    COMMON_FAIL(999, "失败"),

    /* 参数错误：1000～1999 */
    PARAM_NOT_VALID(1001, "参数无效"),
    PARAM_IS_BLANK(1002, "参数为空"),
    PARAM_TYPE_ERROR(1003, "参数类型错误"),
    PARAM_NOT_COMPLETE(1004, "参数缺失"),

    /* 用户错误 */
    USER_NOT_LOGIN(2001, "用户未登录"),
    USER_ACCOUNT_EXPIRED(2002, "账号已过期"),
    USER_CREDENTIALS_ERROR(2003, "密码错误"),
    USER_CREDENTIALS_EXPIRED(2004, "密码过期"),
    USER_ACCOUNT_DISABLE(2005, "账号不可用"),
    USER_ACCOUNT_LOCKED(2006, "账号被锁定"),
    USER_ACCOUNT_NOT_EXIST(2007, "账号不存在"),
    USER_ACCOUNT_ALREADY_EXIST(2008, "账号已存在"),
    USER_ACCOUNT_USE_BY_OTHERS(2009, "账号下线"),
    USER_TOKEN_ERROR(2010, "token解析异常"),
    USER_TOKEN_EXPIRE_ERROR(2011, "token已过期，请重新登录"),

    /* 业务错误 */
    NO_PERMISSION(3001, "没有权限"),

    FAIL_ERROR(4001, "导入文件格式错误，请导入xlsx文件！"),
    FAIL_EMPTY(4002, "导入文件内容为空，请检查导入文件！"),
    FAIL_OVER_MAX(4003, "请导入50w行内的文件！"),
    FAIL_SENSITIVE_ERROR(4004, "模板参数含有%s等敏感词，请修改"),
    FAIL_TEMPLATE_ERROR(4005, "请使用普通消息模板上传或检查首列手机号是否正确！"),
    FAIL_NO_FILE_AUTH(4006, "您无权操作该文件！"),
    FAIL_FILE_DOWNLOAD_ERROR(4007, "文件下载异常！"),
    FAIL_NOT_EXIST(4008, "指定文件不存在！"),
    FAIL_MOBILE_ERROR(4009, "手机号非法！"),
    FAIL_FILE_REPEAT_ERROR(4010, "您已上传过同名文件！"),
    FAIL_USER_REPEAT_ERROR(4011, "已存在同名用户！"),
    FAIL_SCHOOL_REPEAT_ERROR(4012, "已存在同名学校！"),
    FAIL_NO_USER_ERROR(4013, "未找到对应账号！"),
    FAIL_NO_SCHOOL_ERROR(4014, "未找到对应学校！"),
    FAIL_LENGTH_OVER_ERROR(4015, "文件名长度不能超过150个字符！"),
    FAIL_FILE_NAME_EMPTY_ERROR(4016, "文件名不能为空！"),
    FAIL_FILE_NOT_ENHANCE_ERROR(4017, "文件尚未处理完成，请稍后再试！");

    private Integer code;
    private String message;

    ResultCode(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * 根据code获取message
     *
     * @param code
     * @return
     */
    public static String getMessageByCode(Integer code) {
        for (ResultCode ele : values()) {
            if (ele.getCode().equals(code)) {
                return ele.getMessage();
            }
        }
        return null;
    }
}
