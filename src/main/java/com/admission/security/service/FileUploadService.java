package com.admission.security.service;

import com.admission.security.VO.FileRequestVO;
import com.admission.security.common.entity.JsonResult;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

public interface FileUploadService {
    JsonResult uploadFile(MultipartFile file, FileRequestVO param);

    JsonResult getDownloadData(HttpServletResponse response, FileRequestVO param) throws Exception;
}
