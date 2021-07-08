package com.spring.security.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.spring.security.VO.FileRequestVO;
import com.spring.security.common.entity.JsonResult;
import com.spring.security.common.enums.ResultCode;
import com.spring.security.common.utils.ResultTool;
import com.spring.security.config.service.UserToolService;
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
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/file")
public class FileController {


    @Autowired
    FileUploadService fileUploadService;
    @Autowired
    SysUserFileService sysUserFileService;
    @Autowired
    UserToolService userToolService;

    /**
     * 上传接口
     * 仅负责临时存储文件，做简单校验，后续文件处理由跑批负责
     */
    @PostMapping("/upload")
    public JsonResult upload(HttpServletRequest request, MultipartFile file, FileRequestVO param) {
        if (!userToolService.checkUserlogin(request)) {
            return ResultTool.fail(ResultCode.USER_NOT_LOGIN);
        }
        return fileUploadService.uploadFile(request, file, param);
    }


    @GetMapping("/page")
    public JsonResult findAll(HttpServletRequest request, FileRequestVO param) {
        //1、检查是否已登录
        if (!userToolService.checkUserlogin(request)) {
            return ResultTool.fail(ResultCode.USER_NOT_LOGIN);
        }
        //获取前台发送过来的数据
        Integer pageNo = param.getPage();
        Integer pageSize = param.getSize();
        if (null == pageNo || null == pageSize) {
            return ResultTool.fail(ResultCode.PARAM_IS_BLANK);
        }
        IPage<SysUserFile> page = new Page<>(pageNo, pageSize);
        Object list = sysUserFileService.findList(request, page, param);
        return ResultTool.success(list);
    }

    /**
     * 文件下载（失败了会返回一个有部分数据的Excel）
     * <p>
     * 1. 创建excel对应的实体对象
     * <p>
     * 2. 设置返回的 参数
     * <p>
     * 3. 直接写，这里注意，finish的时候会自动关闭OutputStream,当然你外面再关闭流问题不大
     */
    @GetMapping("download")
    public void download(HttpServletRequest request, HttpServletResponse response, FileRequestVO param) throws Exception {
        if (!userToolService.checkUserlogin(request)) {
            throw new Exception(ResultCode.USER_NOT_LOGIN.getMessage());
        }
        fileUploadService.getDownloadData(request, response, param);
    }

}
