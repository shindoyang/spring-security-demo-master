package com.spring.security.service.impl;

import com.alibaba.excel.EasyExcel;
import com.mfexcel.sensitive.engine.SensitiveEngine;
import com.spring.security.common.entity.JsonResult;
import com.spring.security.common.enums.ResultCode;
import com.spring.security.common.utils.ResultTool;
import com.spring.security.dao.SysUserSchoolRelationDao;
import com.spring.security.entity.FileUploadEntity;
import com.spring.security.entity.NmsSmsTmplExcelVo;
import com.spring.security.entity.SysUserSchoolRelation;
import com.spring.security.service.FileUploadService;
import com.spring.security.utils.IdWorker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Slf4j
@Service
public class FileUploadServiceImpl implements FileUploadService {

    @Value("${file.max-rows}")
    int fileMaxRows;

    @Value("${uid.reserveMinutes}")
    int uidExpireMinutes;

    @Resource
    SysUserSchoolRelationDao sysUserSchoolRelationDao;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public JsonResult uploadFile(File file, FileUploadEntity param, String username) {
        IdWorker worker = new IdWorker(1, 1, 1);
        SysUserSchoolRelation relation = sysUserSchoolRelationDao.queryByUsername(username);
        System.out.println(relation.toString());

        if (file != null) {
            // 校验模板文件
            List<NmsSmsTmplExcelVo> list = null;
            int lineIdx = 2; // 模板头部占了两行
            if (file != null) {
                list = EasyExcel.read(file).head(NmsSmsTmplExcelVo.class).sheet(0).headRowNumber(2).doReadSync();
            }

            if (list == null || list.size() <= 0) {
                return ResultTool.fail(ResultCode.FAIL_EMPTY);
            }
            if (list.size() > fileMaxRows) {
                return ResultTool.fail(ResultCode.FAIL_OVER_MAX);
            }

            List<NmsSmsTmplExcelVo> newList = new ArrayList<>();
            //不做敏感词校验
            //todo 根据文件行数获取一次性获取uid
            //todo 文件内容赋值


            for (NmsSmsTmplExcelVo vo : list) {
                lineIdx++;
                String[] textArray = vo.getTextArray();
                for (int i = 0; i < textArray.length; i++) {
                    if (textArray[i] != null) {
                        //todo 必须检查第一列是否手机号
                        List<String> sensitiveList = SensitiveEngine.getInstance().findAllSensitive(textArray[i]);
                        if (sensitiveList != null && sensitiveList.size() > 0) {
                            return new JsonResult(false, ResultCode.FAIL_SENSITIVE_ERROR.getCode(), String.format(ResultCode.FAIL_SENSITIVE_ERROR.getMessage(), ""), sensitiveList.stream().distinct().limit(3).collect(Collectors.joining(",", "【", "】")));
                        }
                    }
                }


                //设置短链
                vo = setUrl(worker, vo);
                newList.add(vo);
            }

            EasyExcel.write("D:/" + param.getFileName() + ".xlsx", NmsSmsTmplExcelVo.class).sheet("Student").doWrite(newList);
            // 释放内存
            list = null;
            newList = null;
        }
        return ResultTool.success();
    }

    private NmsSmsTmplExcelVo setUrl(IdWorker worker, NmsSmsTmplExcelVo vo) {
        //检查内容中是否存在对应的id
        String uid = stringRedisTemplate.opsForValue().get(vo.getMobile());
        if (null == uid || "null".equals(uid)) {
            uid = String.valueOf(worker.nextId());
        }

        if (null == vo.getText1()) {
            vo.setText1(String.valueOf(uid));
            saveUid(vo, uid);
            return vo;
        }
        if (null == vo.getText2()) {
            vo.setText2(String.valueOf(uid));
            saveUid(vo, uid);
            return vo;
        }
        if (null == vo.getText2()) {
            vo.setText2(String.valueOf(uid));
            return vo;
        }
        if (null == vo.getText3()) {
            vo.setText3(String.valueOf(uid));
            saveUid(vo, uid);
            return vo;
        }
        if (null == vo.getText4()) {
            vo.setText4(String.valueOf(uid));
            saveUid(vo, uid);
            return vo;
        }
        if (null == vo.getText5()) {
            vo.setText5(String.valueOf(uid));
            saveUid(vo, uid);
            return vo;
        }
        if (null == vo.getText6()) {
            vo.setText6(String.valueOf(uid));
            saveUid(vo, uid);
            return vo;
        }
        if (null == vo.getText7()) {
            vo.setText7(String.valueOf(uid));
            saveUid(vo, uid);
            return vo;
        }
        if (null == vo.getText8()) {
            vo.setText8(String.valueOf(uid));
            saveUid(vo, uid);
            return vo;
        }
        if (null == vo.getText9()) {
            vo.setText9(String.valueOf(uid));
            saveUid(vo, uid);
            return vo;
        }
        if (null == vo.getText10()) {
            vo.setText10(String.valueOf(uid));
            saveUid(vo, uid);
            return vo;
        }
        return vo;
    }

    public void saveUid(NmsSmsTmplExcelVo vo, String uid) {
        stringRedisTemplate.opsForValue().set(vo.getMobile(), uid, uidExpireMinutes,
                TimeUnit.MINUTES);
    }

}
