package com.spring.security.service.impl;

import com.alibaba.excel.EasyExcel;
import com.mfexcel.sensitive.engine.SensitiveEngine;
import com.spring.security.VO.FileRequestVO;
import com.spring.security.common.entity.JsonResult;
import com.spring.security.common.enums.ResultCode;
import com.spring.security.common.utils.ResultTool;
import com.spring.security.config.AdmissionConfig;
import com.spring.security.config.service.UserToolService;
import com.spring.security.entity.NmsSmsTmplExcelVo;
import com.spring.security.entity.SysUserFile;
import com.spring.security.service.FileUploadService;
import com.spring.security.utils.FileUtils;
import com.spring.security.utils.IdUtils;
import com.spring.security.utils.MobileUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class FileUploadServiceImpl implements FileUploadService {

    @Value("${file.max-rows}")
    int fileMaxRows;

    @Autowired
    UserToolService userToolService;

    @Resource
    SysUserFileServiceImpl sysUserFileService;

    @Override
    public JsonResult uploadFile(MultipartFile file, FileRequestVO param) {

        //todo 文件重复上传校验

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
            String fileName = IdUtils.randomUUID();
            saveFile = FileUtils.createNewFile(AdmissionConfig.getUploadPath() + "/", (fileName + "_temp.xlsx"));
            // 转存
            FileUtils.saveFile(file, saveFile);


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
            SysUserFile sysUserFile = new SysUserFile();
            sysUserFile.setAccount(userToolService.getLoginUser());
            sysUserFile.setFileName(originalFilename);
            sysUserFile.setFileUrl(fileName);
            sysUserFile.setCreateTime(new Date());
            sysUserFileService.save(sysUserFile);
        }
        return ResultTool.success();
    }

}
