package com.spring.security.controller;

import com.spring.security.common.entity.JsonResult;
import com.spring.security.entity.FileUploadEntity;
import com.spring.security.service.FileUploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/file")
public class FileController {


    @Autowired
    FileUploadService fileUploadService;


    /**
     * 上传接口
     * 仅负责临时存储文件，做简单校验，后续文件处理由跑批负责
     */
    @PostMapping("/upload")
    public JsonResult upload(MultipartFile file, FileUploadEntity param) {
        return fileUploadService.uploadFile(file, param);
    }
}
