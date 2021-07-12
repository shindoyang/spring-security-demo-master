package com.admission.security.config.factory;

/**
 * 异步工厂（产生任务用）
 *
 * @author ruoyi
 * <p>
 * 记录登录信息
 * @param username 用户名
 * @param status   状态
 * @param message  消息
 * @return 任务task
 * <p>
 * 操作日志记录
 * @param
 * @return
 */
/*public class AsyncFactory {
    @Autowired
    private static ISysOperLogService sysOperLogService;

    *//**
 * 记录登录信息
 *
 * @param username 用户名
 * @param status   状态
 * @param message  消息
 * @return 任务task
 *//*
    public static TimerTask recordOper(final String username, final String etprInfoId, final Integer status, final String message) {
        final String ip = IpUtils.getIpAddr(ServletUtils.getRequest());
        return new TimerTask() {
            @Override
            public void run() {
                // 封装对象
                SysOperLog loginInfo = new SysOperLog();
                loginInfo.setOperName(username);
                //解决线上莫名其妙出现两个IP的情况
                if (ip.contains(",")) {
                    String trueIp = ip.split(",")[0];
                    loginInfo.setOperIp(trueIp);
                    String address = AddressUtils.getRealAddressByIP(trueIp);
                    loginInfo.setOperLocation(address);
                } else {
                    loginInfo.setOperIp(ip);
                    String address = AddressUtils.getRealAddressByIP(ip);
                    loginInfo.setOperLocation(address);
                }
                loginInfo.setOperatorType(Constants.LOGIN_OPERATOR_TYPE);
                loginInfo.setMsg(message);
                loginInfo.setEtprInfoId(etprInfoId);
                // 日志状态
                loginInfo.setStatus(status);
                loginInfo.setOperTime(new Date());
                // 插入数据
                SpringUtils.getBean(ISysOperLogService.class).insertOperlog(loginInfo);
            }
        };
    }

    *//**
 * 操作日志记录
 *
 * @param
 * @return
 *//*
    public static TimerTask recordOper(final String username, final String etprInfoId, final String tittle, final Integer status, final Integer businessType) {
        final String ip = IpUtils.getIpAddr(ServletUtils.getRequest());
        return new TimerTask() {
            @Override
            public void run() {
                // 封装对象
                SysOperLog operLog = new SysOperLog();
                operLog.setOperName(username);
                operLog.setTitle(tittle);
                operLog.setBusinessType(businessType);
                //解决线上莫名其妙出现两个IP的情况
                if (ip.contains(",")) {
                    String trueIp = ip.split(",")[0];
                    operLog.setOperIp(trueIp);
                    String address = AddressUtils.getRealAddressByIP(trueIp);
                    operLog.setOperLocation(address);
                } else {
                    operLog.setOperIp(ip);
                    String address = AddressUtils.getRealAddressByIP(ip);
                    operLog.setOperLocation(address);
                }
                operLog.setOperatorType(Constants.BUSINESS_OPERATOR_TYPE);
                operLog.setEtprInfoId(etprInfoId);
                // 日志状态
                operLog.setStatus(status);
                operLog.setOperTime(new Date());
                // 远程查询操作地点
                SpringUtils.getBean(ISysOperLogService.class).insertOperlog(operLog);
            }
        };
    }
}*/
