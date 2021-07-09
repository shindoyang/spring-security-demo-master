package com.admission.security.service;

import com.admission.security.VO.FileRequestVO;
import com.admission.security.common.entity.JsonResult;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface FileUploadService {
    JsonResult uploadFile(HttpServletRequest request, MultipartFile file, FileRequestVO param);

    JsonResult getDownloadData(HttpServletRequest request, HttpServletResponse response, FileRequestVO param) throws Exception;
}
