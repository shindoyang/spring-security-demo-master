package com.spring.security.controller;

import com.spring.security.common.entity.JsonResult;
import com.spring.security.common.enums.ResultCode;
import com.spring.security.common.utils.ResultTool;
import com.spring.security.config.AdmissionConfig;
import com.spring.security.entity.FileUploadEntity;
import com.spring.security.service.FileUploadService;
import com.spring.security.utils.FileUtils;
import com.spring.security.utils.IdUtils;
import com.spring.security.utils.UUID;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

@RestController
@RequestMapping("/file")
public class FileController {

    @Autowired
    FileUploadService fileUploadService;


    @PostMapping("/upload")
    public JsonResult upload(MultipartFile file, FileUploadEntity param) {
        File saveFile = null;
        if (file != null && StringUtils.isNotBlank(file.getOriginalFilename())) { // 校验模板
            if (!file.getOriginalFilename().toLowerCase().endsWith(".xlsx")) {
                return ResultTool.fail(ResultCode.FAIL_ERROR);
            }
            // 临时文件名
            String fileName = IdUtils.randomUUID() + ".xlsx";
            saveFile = FileUtils.createNewFile(AdmissionConfig.getUploadPath() + "/", fileName);
            // 转存
            if (file != null) {
                FileUtils.saveFile(file, saveFile);
            }
        }
        /*AjaxResult validateResult = t5GMsgSendService.validateS5G(saveFile, param, false);
        if (HttpStatus.SUCCESS != (Integer) validateResult.get(AjaxResult.CODE_TAG)) {
            return validateResult;
        }*/
        return fileUploadService.uploadFile(saveFile, param);
    }


    public static void main(String[] args) {
        String str1 = "sdfsfwerwrwfsdwrewDDs";
        String s = UUID.nameUUIDFromBytes((str1).getBytes()).toString();
        System.out.println(s);
    }
}
