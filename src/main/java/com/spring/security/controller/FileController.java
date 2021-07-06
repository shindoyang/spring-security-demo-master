package com.spring.security.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.spring.security.common.entity.JsonResult;
import com.spring.security.common.utils.ResultTool;
import com.spring.security.entity.FileUploadEntity;
import com.spring.security.entity.SysUserFile;
import com.spring.security.service.FileUploadService;
import com.spring.security.service.SysUserFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/file")
public class FileController {


    @Autowired
    FileUploadService fileUploadService;
    @Autowired
    SysUserFileService sysUserFileService;

    /**
     * 上传接口
     * 仅负责临时存储文件，做简单校验，后续文件处理由跑批负责
     */
    @PostMapping("/upload")
    public JsonResult upload(MultipartFile file, FileUploadEntity param) {
        return fileUploadService.uploadFile(file, param);
    }


    @GetMapping("/page")
    public JsonResult findAll(HttpServletRequest request) {
        //获取前台发送过来的数据
        Integer pageNo = Integer.valueOf(request.getParameter("page"));
        Integer pageSize = Integer.valueOf(request.getParameter("size"));
        IPage<SysUserFile> page = new Page<>(pageNo, pageSize);
        Object list = sysUserFileService.findList(page);
        if (null != list) {
            return ResultTool.success(list);
        }
        return ResultTool.success();
    }
}
