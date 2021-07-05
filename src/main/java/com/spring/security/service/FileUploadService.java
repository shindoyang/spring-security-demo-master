package com.spring.security.service;

import com.spring.security.common.entity.JsonResult;
import com.spring.security.entity.FileUploadEntity;
import org.springframework.web.multipart.MultipartFile;

public interface FileUploadService {
    JsonResult uploadFile(MultipartFile file, FileUploadEntity param);
}
