package com.spring.security.schedule;

import com.alibaba.excel.EasyExcel;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.spring.security.config.AdmissionConfig;
import com.spring.security.config.service.UserToolService;
import com.spring.security.constant.RedisConstant;
import com.spring.security.entity.NmsSmsTmplExcelVo;
import com.spring.security.entity.SysUserFile;
import com.spring.security.service.SysUserFileService;
import com.spring.security.utils.IdWorker;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

import javax.annotation.Resource;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class UserFileEnhanceScheduled {

    @Value("${uid.reserveMinutes}")
    int uidExpireMinutes;

    @Autowired
    SysUserFileService sysUserFileService;

    @Autowired
    UserToolService userToolService;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 文件增强处理器
     */
    @Scheduled(cron = "${schedule.userfile.cron}")
    private void sendMessageScheduled() {
        //todo 加并发锁

        log.info("开始查找待增强的文件");
        QueryWrapper<SysUserFile> wrapper = new QueryWrapper<>();
        wrapper.eq("status", 0);
        List<SysUserFile> list = sysUserFileService.list(wrapper);
        log.info("待处理文件数：{}", list.size());
        log.info("待处理文件内容：{}", JSON.toJSON(list));
        if (null != list && list.size() > 0) {

            SysUserFile sysUserFile = list.get(0);
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


                stopWatch.start();
                List<NmsSmsTmplExcelVo> newList = setUrl(sysUserFile, nmsFileList);
                stopWatch.stop();
                log.info("添加短链耗时：{} 秒", stopWatch.getTotalTimeSeconds());

                stopWatch.start();
                EasyExcel.write(finishFilePath, NmsSmsTmplExcelVo.class).sheet("Sheet1").doWrite(newList);
                stopWatch.stop();
                log.info("写文件耗时：{} 秒", stopWatch.getTotalTimeSeconds());
            }
            sysUserFile.setStatus(1);
            sysUserFileService.updateById(sysUserFile);
            log.info("文件：{}，处理完成！", sysUserFile.getFileName());
        }


    }

    private List<NmsSmsTmplExcelVo> setUrl(SysUserFile sysUserFile, List<NmsSmsTmplExcelVo> nmsFileList) {
        //获取缓存的已经处理过的手机号
        String cacheMobileKey = String.format(RedisConstant.USER_CACHE_MOBILES, sysUserFile.getAccount());
        String cacheUidKey = String.format(RedisConstant.USER_CACHE_UIDS, sysUserFile.getAccount());

        String cacheMobiles = stringRedisTemplate.opsForValue().get(cacheMobileKey);
        //获取缓存的已经生成的手机号-uid对应关系
        String cacheUids = stringRedisTemplate.opsForValue().get(cacheUidKey);
        JSONArray cacheMobileArr = new JSONArray();
        Map cacheUidMap = new HashMap();
        if (Strings.isNotEmpty(cacheMobiles)) {
            cacheMobileArr = JSONObject.parseArray(cacheMobiles);
        }
        if (Strings.isNotEmpty(cacheUids)) {
            cacheUidMap = (Map) JSON.parse(cacheUids);
        }
        log.info("已处理过的手机号个数：{}", null != cacheMobileArr ? cacheMobileArr.size() : 0);
        log.info("已缓存过的uid个数：{}", null != cacheUidMap ? cacheUidMap.size() : 0);

        IdWorker worker = new IdWorker(1, 1, 1);
        List<NmsSmsTmplExcelVo> writeFileDataList = new ArrayList<>();
        List<NmsSmsTmplExcelVo> insertDBList = new ArrayList<>();

        for (NmsSmsTmplExcelVo vo : nmsFileList) {
            String uid = null;
            //如果手机号已处理过
            if (null != cacheMobileArr && cacheMobileArr.contains(vo.getMobile())) {
                if (null != cacheUidMap) {
                    uid = String.valueOf(cacheUidMap.get(vo.getMobile()));
                }
            } else {
                uid = String.valueOf(worker.nextId());
                cacheMobileArr.add(vo.getMobile());
                cacheUidMap.put(vo.getMobile(), uid);
                insertDBList.add(vo);
            }
            //设置短链
            vo = setUrl(vo, uid);
            writeFileDataList.add(vo);
        }

        //未处理的数据批量入库
        batchSave(insertDBList);

        //缓存本次处理内容
        stringRedisTemplate.opsForValue().set(cacheMobileKey, JSONObject.toJSONString(cacheMobileArr), uidExpireMinutes, TimeUnit.MINUTES);
        stringRedisTemplate.opsForValue().set(cacheUidKey, JSONObject.toJSONString(cacheUidMap), uidExpireMinutes, TimeUnit.MINUTES);
        return writeFileDataList;
    }

    /**
     * 批量入库
     */
    public void batchSave(List<NmsSmsTmplExcelVo> insertDBList) {

    }


    /**
     * 给每行数据设置链接
     */
    private NmsSmsTmplExcelVo setUrl(NmsSmsTmplExcelVo vo, String uid) {
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
