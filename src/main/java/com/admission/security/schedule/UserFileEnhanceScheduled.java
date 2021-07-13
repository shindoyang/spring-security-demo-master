package com.admission.security.schedule;

import com.admission.security.config.AdmissionConfig;
import com.admission.security.config.service.UserToolService;
import com.admission.security.constant.RedisConstant;
import com.admission.security.entity.*;
import com.admission.security.service.SysSchoolService;
import com.admission.security.service.SysStudentService;
import com.admission.security.service.SysUserFileService;
import com.admission.security.utils.IdWorker;
import com.admission.security.utils.IpUtils;
import com.admission.security.utils.UidUtils;
import com.alibaba.excel.EasyExcel;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

import javax.annotation.Resource;
import java.io.File;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class UserFileEnhanceScheduled {

    @Value("${uid.reserveMinutes}")
    int uidExpireMinutes;

    @Autowired
    SysUserFileService sysUserFileService;

    @Autowired
    SysStudentService sysStudentService;

    @Autowired
    SysSchoolService sysSchoolService;

    @Autowired
    UserToolService userToolService;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    private final static String concurrentKey = "schedule.userfile.cron";

    @Value("${redis.concurrent.processingExpireMilliSeconds}")
    private long processingExpireMilliSeconds; //毫秒

    /**
     * 文件增强处理器
     */
    @Scheduled(cron = "${schedule.userfile.cron}")
    private void sendMessageScheduled() {
        boolean isJobRunning = false;
        log.info("开始查找待增强的文件");
        try {
            // 获取redis锁
            if (stringRedisTemplate.opsForValue().setIfAbsent(concurrentKey, IpUtils.getHostIp(), processingExpireMilliSeconds, TimeUnit.MILLISECONDS)) {
                isJobRunning = true;
                log.info("成功获取文件增强定时器并发锁！");
                QueryWrapper<SysUserFile> wrapper = new QueryWrapper<>();
                wrapper.eq("status", 0);
                List<SysUserFile> list = sysUserFileService.list(wrapper);
                log.info("待处理文件数：{}", list.size());

                if (null != list && list.size() > 0) {
                    for (int i = 0; i < list.size(); i++) {
                        int index = i + 1;
                        log.info("开始处理第 {} 条文件，内容：{}", index, JSON.toJSON(list));
                        SysUserFile sysUserFile = list.get(0);
                        try {
                            //获取关联的学校信息
                            QueryWrapper<SysSchool> schoolWrapper = new QueryWrapper<>();
                            schoolWrapper.eq("account", sysUserFile.getAccount());
                            SysSchool sysSchool = sysSchoolService.getOne(schoolWrapper);
                            if (null == sysSchool) {
                                throw new Exception(sysUserFile.getAccount() + "未设置关联的学校信息！");
                            }

                            String filePath = AdmissionConfig.getUploadPath() + "/" + sysUserFile.getFileUrl();
                            String tempFilePath = filePath + "_temp.xlsx";
                            String finishFilePath = filePath + ".xlsx";


                            File file = new File(tempFilePath);
                            if (file != null) {
                                // 读取文件
                                StopWatch stopWatch = new StopWatch();
                                stopWatch.start();
                                List<NmsSmsTmplExcelVo> nmsFileList = EasyExcel.read(file).head(NmsSmsTmplExcelVo.class).sheet(0).headRowNumber(2).doReadSync();
                                stopWatch.stop();
                                log.info("读取文件耗时：{} 秒", stopWatch.getTotalTimeSeconds());
                                log.info("当前文件总行数：{}", null != nmsFileList ? nmsFileList.size() : 0);

                                //添加短链
                                stopWatch.start();
                                List<NmsSmsTmplExcelVo> newList = setUrl(sysSchool, sysUserFile, nmsFileList);
                                stopWatch.stop();
                                log.info("添加短链耗时：{} 秒", stopWatch.getTotalTimeSeconds());

                                stopWatch.start();
                                EasyExcel.write(finishFilePath, NmsSmsTmplExcelVo.class).sheet("Sheet1").doWrite(newList);
                                stopWatch.stop();
                                log.info("写文件耗时：{} 秒", stopWatch.getTotalTimeSeconds());
                            }
                            sysUserFile.setStatus(1);
                            sysUserFile.setUpdateTime(new Date());
                            sysUserFileService.updateById(sysUserFile);
                            log.info("文件：{}，处理完成！", sysUserFile.getFileName());

                        } catch (Exception e) {
                            log.error("文件：{}，处理失败：{}", sysUserFile.getFileName(), e.getMessage());
                            e.printStackTrace();
                        }

                        log.info("第 {} 条文件处理完成！", index);
                    }
                }
            } else {
                log.info("文件增强定时器正在处理当中");
            }
        } catch (Exception e) {
            log.error("文件增强定时器任务异常" + e.getMessage());
        } finally {
            if (isJobRunning) {
                log.info("释放文件增强定时器并发锁！");
                stringRedisTemplate.delete(concurrentKey);
            }
        }

    }

    private List<NmsSmsTmplExcelVo> setUrl(SysSchool sysSchool, SysUserFile sysUserFile, List<NmsSmsTmplExcelVo> nmsFileList) throws Exception {
        //获取缓存的已经处理过的手机号
        String cacheMobileKey = String.format(RedisConstant.USER_CACHE_MOBILES, sysUserFile.getAccount());
        String cacheUidKey = String.format(RedisConstant.USER_CACHE_UIDS, sysUserFile.getAccount());
        String cacheWholeUidKey = RedisConstant.WHOLE_CACHE_UIDS;

        String cacheMobiles = stringRedisTemplate.opsForValue().get(cacheMobileKey);
        //获取缓存的已经生成的手机号-uid对应关系
        String cacheUids = stringRedisTemplate.opsForValue().get(cacheUidKey);
        //获取系统生成过的所有uid
        String wholeUids = stringRedisTemplate.opsForValue().get(cacheWholeUidKey);
        JSONArray cacheMobileArr = new JSONArray();
        if (Strings.isNotEmpty(cacheMobiles)) {
            cacheMobileArr = JSONObject.parseArray(cacheMobiles);
        }
        Map<String, String> cacheUidMap = new HashMap();
        if (Strings.isNotEmpty(cacheUids)) {
            cacheUidMap = (Map) JSON.parse(cacheUids);
        }
        JSONArray cacheWholeUidArr = new JSONArray();
        if (Strings.isNotEmpty(wholeUids)) {
            cacheWholeUidArr = JSONObject.parseArray(wholeUids);
        }
        log.info("已处理过的手机号个数：{}", null != cacheMobileArr ? cacheMobileArr.size() : 0);
        log.info("已缓存过的uid个数：{}", null != cacheUidMap ? cacheUidMap.size() : 0);

        IdWorker worker = new IdWorker(1, 1, 1);
        List<NmsSmsTmplExcelVo> wholeExecelDataList = new ArrayList<>();
        List<NmsSmsTmplDBVo> updateDBList = new ArrayList<>();

        for (NmsSmsTmplExcelVo vo : nmsFileList) {
            String uid = null;
            //缓存和数据库只处理增量的,excel文件要全量处理。保证同一个手机号每次生成的链接都是一样的
            if (null != cacheMobileArr && cacheMobileArr.contains(vo.getMobile())) {
                if (null != cacheUidMap) {
                    uid = cacheUidMap.get(vo.getMobile());
                    //设置短链
                    vo = setUrl(sysSchool, vo, uid);
                }
            } else {
//                uid = ShortUrlGenerate.generate(String.format(RedisConstant.USER_SHORT_URL, sysSchool.getHost(), IdUtils.randomUUID(), vo.getMobile()));
                uid = UidUtils.getUid(cacheWholeUidArr, sysSchool.getHost(), vo.getMobile());
                //本次增量内容
                cacheMobileArr.add(vo.getMobile());
                cacheUidMap.put(vo.getMobile(), uid);
                cacheWholeUidArr.add(uid);
                NmsSmsTmplDBVo dbVo = new NmsSmsTmplDBVo();
                dbVo.setStuUid(uid);

                //设置短链
                vo = setUrl(sysSchool, vo, uid);
                BeanUtils.copyProperties(vo, dbVo);

                updateDBList.add(dbVo);
            }

            wholeExecelDataList.add(vo);
        }

        //增量数据批量入库
        if (updateDBList.size() > 0) {
            batchSave(sysUserFile, updateDBList);
        }

        //缓存增量更新
//        stringRedisTemplate.opsForValue().set(cacheMobileKey, JSONObject.toJSONString(cacheMobileArr), uidExpireMinutes, TimeUnit.MINUTES);
//        stringRedisTemplate.opsForValue().set(cacheUidKey, JSONObject.toJSONString(cacheUidMap), uidExpireMinutes, TimeUnit.MINUTES);
//        stringRedisTemplate.opsForValue().set(cacheWholeUidKey, JSONObject.toJSONString(cacheWholeUidArr), uidExpireMinutes, TimeUnit.MINUTES);
        stringRedisTemplate.opsForValue().set(cacheMobileKey, JSONObject.toJSONString(cacheMobileArr));
        stringRedisTemplate.opsForValue().set(cacheUidKey, JSONObject.toJSONString(cacheUidMap));
        stringRedisTemplate.opsForValue().set(cacheWholeUidKey, JSONObject.toJSONString(cacheWholeUidArr));
        return wholeExecelDataList;
    }

    /**
     * 批量入库
     */
    public void batchSave(SysUserFile sysUserFile, List<NmsSmsTmplDBVo> insertDBList) throws Exception {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        List<SysStudent> students = new ArrayList<>();
        insertDBList.forEach(nmsRow -> {
            SysStudent stu = new SysStudent();
            BeanUtils.copyProperties(nmsRow, stu);
            stu.setAccount(sysUserFile.getAccount());
            students.add(stu);
        });

        boolean batchSaveSuccess = sysStudentService.saveBatch(students);
        if (!batchSaveSuccess) {
            throw new Exception("学生数据批量入库异常！");
        }
        stopWatch.stop();
        log.info("学生数据批量入库耗时：{} 秒", stopWatch.getTotalTimeSeconds());
    }


    /**
     * 给每行数据设置链接
     */
    private NmsSmsTmplExcelVo setUrl(SysSchool sysSchool, NmsSmsTmplExcelVo vo, String uid) {
        if (sysSchool != null) {
            uid = sysSchool.getHost() + uid;
        }

        if (null == vo.getText1()) {
            vo.setText1(uid);
            return vo;
        }
        if (null == vo.getText2()) {
            vo.setText2(uid);
            return vo;
        }
        if (null == vo.getText2()) {
            vo.setText2(uid);
            return vo;
        }
        if (null == vo.getText3()) {
            vo.setText3(uid);
            return vo;
        }
        if (null == vo.getText4()) {
            vo.setText4(uid);
            return vo;
        }
        if (null == vo.getText5()) {
            vo.setText5(uid);
            return vo;
        }
        if (null == vo.getText6()) {
            vo.setText6(uid);
            return vo;
        }
        if (null == vo.getText7()) {
            vo.setText7(uid);
            return vo;
        }
        if (null == vo.getText8()) {
            vo.setText8(uid);
            return vo;
        }
        if (null == vo.getText9()) {
            vo.setText9(uid);
            return vo;
        }
        if (null == vo.getText10()) {
            vo.setText10(uid);
            return vo;
        }
        return vo;
    }
}
