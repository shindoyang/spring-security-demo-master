package com.admission.security.constant;

/**
 * 通用常量信息
 *
 * @author mfexcel
 */
public class Constants {
    /**
     * UTF-8 字符集
     */
    public static final String UTF8 = "UTF-8";

    /**
     * GBK 字符集
     */
    public static final String GBK = "GBK";

    /**
     * http请求
     */
    public static final String HTTP = "http://";

    /**
     * https请求
     */
    public static final String HTTPS = "https://";

    /**
     * 通用成功标识
     */
    public static final String SUCCESS = "0";

    /**
     * 通用失败标识
     */
    public static final String FAIL = "1";

    /**
     * 登录成功
     */
    public static final String LOGIN_SUCCESS = "Success";

    /**
     * 注销
     */
    public static final String LOGOUT = "Logout";

    /**
     * 登录失败
     */
    public static final String LOGIN_FAIL = "Error";

    /**
     * 验证码 redis key
     */
    public static final String CAPTCHA_CODE_KEY = "captcha_codes:";

    /**
     * 登录用户 redis key
     */
    public static final String LOGIN_TOKEN_KEY = "login_tokens:";

    /**
     * 验证码有效期（分钟）
     */
    public static final Integer CAPTCHA_EXPIRATION = 2;

    /**
     * 令牌
     */
    public static final String TOKEN = "token";

    /**
     * 令牌前缀
     */
    public static final String TOKEN_PREFIX = "Bearer ";

    /**
     * 令牌前缀
     */
    public static final String LOGIN_USER_KEY = "login_user_key";

    /**
     * 用户ID
     */
    public static final String JWT_USERID = "userid";

    /**
     * 用户名称
     */
    public static final String JWT_USERNAME = "sub";

    /**
     * 用户头像
     */
    public static final String JWT_AVATAR = "avatar";

    /**
     * 创建时间
     */
    public static final String JWT_CREATED = "created";

    /**
     * 用户权限
     */
    public static final String JWT_AUTHORITIES = "authorities";

    /**
     * 参数管理 cache key
     */
    public static final String SYS_CONFIG_KEY = "sys_config:";

    /**
     * 字典管理 cache key
     */
    public static final String SYS_DICT_KEY = "sys_dict:";

    /**
     * 资源映射路径 前缀
     */
    public static final String RESOURCE_PREFIX = "/profile";

    /**
     * 消息类型-标准5G消息
     */
    public static final String MSG_TYPE_S5G = "S5G";
    /**
     * 消息类型-增强5G消息的多媒体消息
     */
    public static final String MSG_TYPE_E5G = "E5G";
    /**
     * 消息类型-增强5G消息的纯文本消息
     */
    public static final String MSG_TYPE_TEXT = "TEXT";
    /**
     * 消息类型-所有增强5G消息
     */
    public static final String MSG_TYPE_E5G_ALL = "E5GALL";
    /**
     * 消息类型-闪信
     */
    public static final String MSG_TYPE_FLASH = "FLASH";
    /**
     * 消息类型-普通短信消息
     */
    public static final String MSG_TYPE_SMS = "SMS";

    /**
     * 发送记录状态
     */
    public static final String MSG_SEND_STATUS_PENDING = "0"; // 待发送
    public static final String MSG_SEND_STATUS_SUCC = "1"; // 已发送
    public static final String MSG_SEND_STATUS_CANCEL = "2"; // 已取消
    public static final String MSG_SEND_STATUS_FAIL = "3"; // 发送失败

    /**
     * 发送记录明细状态
     */
    public static final String MSG_SEND_DETAIL_STATUS_PENDING = "0"; // 未知
    public static final String MSG_SEND_DETAIL_STATUS_SUCC = "1"; // 成功
    public static final String MSG_SEND_DETAIL_STATUS_FAIL = "2"; // 失败

    /**
     * 消息发送素材重审状态
     */
    public static final String MSG_SEND_MATERIAL_STATUS_INIT = "0"; // 待审核
    public static final String MSG_SEND_MATERIAL_STATUS_PENDING = "1"; // 审核中
    public static final String MSG_SEND_MATERIAL_STATUS_FAIL = "2"; // 审核不通过
    public static final String MSG_SEND_MATERIAL_STATUS_SUCC = "3"; // 审核通过

    /**
     * 闪信模板状态
     */
    public static final String FLASH_TMPL_STATUS_INIT = "0"; // 待提交
    public static final String FLASH_TMPL_STATUS_PENDING = "1"; // 待审核
    public static final String FLASH_TMPL_STATUS_SUCC = "2"; // 审核通过
    public static final String FLASH_TMPL_STATUS_FAIL = "3"; // 审核不通过

    /**
     * 普通短信模板状态
     */
    public static final String SMS_TMPL_STATUS_INIT = "0"; // 待提交
    public static final String SMS_TMPL_STATUS_PENDING = "1"; // 待审核
    public static final String SMS_TMPL_STATUS_SUCC = "2"; // 审核通过
    public static final String SMS_TMPL_STATUS_FAIL = "3"; // 审核不通过

    /**
     * 闪信主叫号码状态
     */
    public static final String FLASH_HOTLINE_STATUS_INIT = "0"; // 报备中
    public static final String FLASH_HOTLINE_STATUS_SUCC = "1"; // 报备通过
    public static final String FLASH_HOTLINE_STATUS_FAIL = "2"; // 报备不通过

    /**
     * 闪信供应商类型
     */
    public static final String FLASH_SUPPLIER_WEWAY = "1"; // 微位
    public static final String FLASH_SUPPLIER_MIGU = "2"; // 咪咕

    /**
     * 语音模板状态
     */
    public static final String VOICE_TMPL_STATUS_INIT = "0"; // 待提交
    public static final String VOICE_TMPL_STATUS_PENDING = "1"; // 待审核
    public static final String VOICE_TMPL_STATUS_SUCC = "2"; // 审核通过
    public static final String VOICE_TMPL_STATUS_FAIL = "3"; // 审核不通过

    /**
     * 供应商id
     */
    public static final String SYS_SUP_KEY = "supplier:";

    /**
     * 供应商类型
     */
    public static final String SYS_SUP_STANDARD = "sup_standard_type";
    public static final String SYS_SUP_ENHANCE = "sup_enhance_type";

    /**
     * 供应商账号中码号状态 0 启用 1禁用
     */
    public static final String SUP_ACCOUNT_STATUS_YES = "0";
    public static final String SUP_ACCOUNT_STATUS_NO = "1";

    /**
     * 签名管理中判断登入用户是否是客户侧用户 true 客户侧 false 运营侧
     */
    public static final boolean SIG_CUS_IDENTIFY_TRUE = true;
    public static final boolean SIG_CUS_IDENTIFY_FALSE = false;

    /**
     * 供应商账户账户属性
     */
    public static final String[] ACCOUNT = {"0", "1", "2", "3"};

    /**
     * 供应商账户模板标识
     */
    public static final String[] TEMPLATE = {"0", "1"};

    /**
     * 运营商
     */
    public final static String OP_CMCC = "CMCC";
    public final static String OP_CTCC = "CTCC";
    public final static String OP_CUCC = "CUCC";

    /**
     * 通用标识 0三网 1移动 2联通 3电信
     */
    public static final String SUP_ACCOUNT_GENERAL = "0";
    public static final String SUP_ACCOUNT_CMCC = "1";
    public static final String SUP_ACCOUNT_CUCC = "2";
    public static final String SUP_ACCOUNT_CTCC = "3";

    /**
     * 5G账户菜单管理 0审核中 1已上线 2审核不通过 3已上线(修改审核中)
     */
    public static final String MSG_5G_ACCOUNT_CHECK = "0";
    public static final String MSG_5G_ACCOUNT_DEPLOY = "1";
    public static final String MSG_5G_ACCOUNT_FAILED = "2";
    public static final String MSG_5G_ACCOUNT_DEPLOY_CHECK = "3";

    /**
     * 运营商所属大区 region_cmcc:移动 region_cucc:联通 region_ctcc:电信
     */
    public static final String REGION_CMCC = "region_cmcc";
    public static final String REGION_CUCC = "region_cucc";
    public static final String REGION_CTCC = "region_ctcc";

    /**
     * 逻辑删除标识（0:未删除 1:已删除）
     */
    public static final int IS_DELETED_YES = 1;
    public static final int IS_DELETED_NO = 0;

    /**
     * 性别常量
     */
    public static final String SEX_MAN = "0";
    public static final String SEX_WOMAN = "1";
    public static final String SEX_UNKNOWN = "2";

    /**
     * CSP用户类型
     */
    public static final String CSP_USER_TYPE_CLIENT = "客户";
    public static final String CSP_USER_TYPE_OPERATION = "运营";
    /**
     * 客户用户类型
     */
    public static final String USER_TYPE_CLIENT = "00";
    public static final String USER_TYPE_MANAGER = "01";

    /**
     *
     */
    public static final String CSP_USER_MENU_SYS_ACCOUNT = "账号权限管理";

    /**
     * CSP用户日志类型
     */
    public static final Integer LOGIN_OPERATOR_TYPE = 0;
    public static final Integer BUSINESS_OPERATOR_TYPE = 1;

    /**
     * CSP操作类型
     * 1=新增,2=修改,3=删除,4=提交审核，5=上传,6=下载,7=审核,8=批量删除,9=启用(上线)，10=禁用（下线），11=批量启用，12=批量禁用，13=移动
     */
    public static final int BUSINESS_TYPE_INSERT = 1;
    public static final int BUSINESS_TYPE_EDIT = 2;
    public static final int BUSINESS_TYPE_DELETE = 3;
    public static final int BUSINESS_TYPE_APPROVE = 4;
    public static final int BUSINESS_TYPE_UPLOAD = 5;
    public static final int BUSINESS_TYPE_DOWNLOAD = 6;
    public static final int BUSINESS_TYPE_AUDIT = 7;
    public static final int BUSINESS_TYPE_BATCH_DELETE = 8;
    public static final int BUSINESS_TYPE_ENABLE = 9;
    public static final int BUSINESS_TYPE_DISABLE = 10;

    public static final int BUSINESS_TYPE_BATCH_ENABLE = 11;
    public static final int BUSINESS_TYPE_BATCH_DISABLE = 12;
    public static final int BUSINESS_TYPE_MOVE = 13;

    public static final int BUSINESS_TYPE_USE = 14;
    public static final int BUSINESS_TYPE_PUBLISH = 15;
    public static final int BUSINESS_TYPE_SAVE = 16;
    public static final int BUSINESS_QR_CODE_GENERATE = 17;

    /**
     * CSP用户日志结果
     */
    public static final Integer BUSINESS_RESULT_SUCCESS = 0;
    public static final Integer BUSINESS_RESULT_FAIL = 1;

    public static final String CODE_TAG = "code";
    /*
     *
     * */
    public static final String RING_GRAPH_URL = "/customerSenceCount";
    public static final String CUSTOMER_SCENCE_ALL_CNT_URL = "/customerSenceAllCnt";
    public static final String CUSTOMER_SCENCE_PLAN_CNT_URL = "/customerSencePlanCnt";
    public static final String CUSTOMER_SCENCE_LIST_URL = "/customerSenceList";
    public static final String MONTH_WEEK_REPORT_URL = "/customerSenceCountWM";

    public static final String CUSTOMER_ACTIVE_LIST_URL = "/customerActiveList";
    public static final String CUSTOMER_ACTIVE_CNT_URL = "/customerActiveCount";
    public static final String CUSTOMER_ACTIVE_CNT_TREND_URL = "/customerActiveCntTrend";

    // 短信统计-发送统计
    public static final String SEND_TOTAL_URL = "/yySentCount";
    // 短信统计-客户统计-分页列表
    public static final String CUSTOMER_TOTAL_BY_PAGE_URL = "/yyCustomerCount";
    // 短信统计-客户统计-总数行
    public static final String CUSTOMER_TOTAL_URL = "/yyCustomerCountMonth";
    // 短信统计-供应商统计-总数行
    public static final String SUPPLIER_TOTAL_URL = "/yySupplierCountMonth";
    // 短信统计-供应商统计-分页列表
    public static final String SUPPLIER_TOTAL_BY_PAGE_URL = "/yySupplierCount";

    /**
     * 缓存企业信息key为 appid
     */
    public static final String ETPRINFO_REDIS_KEY = "csp:etprinfo:%s";
    public static final String ETPRINFO_VER_REDIS_KEY = "csp:etprinfo:version";
}
