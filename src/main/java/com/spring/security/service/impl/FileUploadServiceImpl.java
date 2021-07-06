package com.spring.security.service.impl;

import com.alibaba.excel.EasyExcel;
import com.mfexcel.sensitive.engine.SensitiveEngine;
import com.spring.security.VO.FileRequestVO;
import com.spring.security.common.entity.JsonResult;
import com.spring.security.common.enums.ResultCode;
import com.spring.security.common.utils.ResultTool;
import com.spring.security.config.AdmissionConfig;
import com.spring.security.dao.SysUserSchoolRelationDao;
import com.spring.security.entity.NmsSmsTmplExcelVo;
import com.spring.security.service.FileUploadService;
import com.spring.security.utils.FileUtils;
import com.spring.security.utils.IdUtils;
import com.spring.security.utils.IdWorker;
import com.spring.security.utils.MobileUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
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
    public JsonResult uploadFile(MultipartFile file, FileRequestVO param) {

        //1、文件保存
        File saveFile = null;
        if (null != file && StringUtils.isNotBlank(file.getOriginalFilename())) {
            // 校验文件后缀
            if (!file.getOriginalFilename().toLowerCase().endsWith(".xlsx")) {
                return ResultTool.fail(ResultCode.FAIL_ERROR);
            }
            String originalFilename = file.getOriginalFilename();
            log.info("originalFilename = " + originalFilename);

            // 临时文件名
            String fileName = IdUtils.randomUUID() + ".xlsx";
            saveFile = FileUtils.createNewFile(AdmissionConfig.getUploadPath() + "/", fileName);
            // 转存
            FileUtils.saveFile(file, saveFile);
        }

        //2、校验文件
        if (null != saveFile) {
            // 读取文件
            List<NmsSmsTmplExcelVo> list = null;
            // 模板头部占了两行
            int lineIdx = 2;
            if (file != null) {
                list = EasyExcel.read(saveFile).head(NmsSmsTmplExcelVo.class).sheet(0).headRowNumber(2).doReadSync();
            }
            // 判断文件是否为空
            if (list == null || list.size() <= 0) {
                return ResultTool.fail(ResultCode.FAIL_EMPTY);
            }
            // 判断文件条数是否超过限制
            if (list.size() > fileMaxRows) {
                return ResultTool.fail(ResultCode.FAIL_OVER_MAX);
            }
            //敏感词校验
            for (NmsSmsTmplExcelVo vo : list) {
                //校验文件模板：首列是否手机号
                if (!MobileUtil.isMobileNO(vo.getMobile())) {
                    return ResultTool.fail(ResultCode.FAIL_TEMPLATE_ERROR);
                }
                String[] textArray = vo.getTextArray();
                for (int i = 0; i < textArray.length; i++) {
                    if (textArray[i] != null) {
                        List<String> sensitiveList = SensitiveEngine.getInstance().findAllSensitive(textArray[i]);
                        if (sensitiveList != null && sensitiveList.size() > 0) {
                            return new JsonResult(false, ResultCode.FAIL_SENSITIVE_ERROR.getCode(), String.format(ResultCode.FAIL_SENSITIVE_ERROR.getMessage(), ""), sensitiveList.stream().distinct().limit(3).collect(Collectors.joining(",", "【", "】")));
                        }
                    }
                }
            }

        }

        return ResultTool.success();
    }

    /**
     * 文件转换
     */
    /*public JsonResult modifyFile(File file, FileUploadEntity param, String username) {
        IdWorker worker = new IdWorker(1, 1, 1);
        SysUserSchoolRelation relation = sysUserSchoolRelationDao.queryByUsername(username);
        System.out.println(relation.toString());

        if (file != null) {

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
    }*/
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
