package com.spring.security.service;

import com.spring.security.common.entity.JsonResult;
import com.spring.security.entity.FileUploadEntity;

import java.io.File;

public interface FileUploadService {
    JsonResult uploadFile(File file, FileUploadEntity param, String username);
}
