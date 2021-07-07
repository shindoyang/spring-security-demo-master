package com.spring.security.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.spring.security.VO.FileRequestVO;
import com.spring.security.entity.SysUserFile;

import javax.servlet.http.HttpServletRequest;

/**
 *
 */
public interface SysUserFileService extends IService<SysUserFile> {
    Object findList(HttpServletRequest request, IPage<SysUserFile> page, FileRequestVO param);
}
