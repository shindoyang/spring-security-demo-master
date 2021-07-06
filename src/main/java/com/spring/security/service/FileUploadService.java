package com.spring.security.service;

import com.spring.security.VO.FileRequestVO;
import com.spring.security.common.entity.JsonResult;
import org.springframework.web.multipart.MultipartFile;

public interface FileUploadService {
    JsonResult uploadFile(MultipartFile file, FileRequestVO param);
}
