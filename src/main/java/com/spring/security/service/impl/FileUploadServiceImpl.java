package com.spring.security.service.impl;

import com.spring.security.common.entity.JsonResult;
import com.spring.security.common.enums.ResultCode;
import com.spring.security.common.utils.ResultTool;
import com.spring.security.entity.FileUploadEntity;
import com.spring.security.service.FileUploadService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.File;

@Slf4j
@Service
public class FileUploadServiceImpl implements FileUploadService {
    @Override
    public JsonResult uploadFile(File file, FileUploadEntity param) {
        return ResultTool.fail(ResultCode.SUCCESS);
    }
}
