package com.spring.security.service;

import com.spring.security.VO.FileRequestVO;
import com.spring.security.common.entity.JsonResult;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface FileUploadService {
    JsonResult uploadFile(HttpServletRequest request, MultipartFile file, FileRequestVO param);

    JsonResult getDownloadData(HttpServletRequest request, HttpServletResponse response, FileRequestVO param) throws Exception;
}
