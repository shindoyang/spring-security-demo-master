package com.admission.security.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.admission.security.VO.FileRequestVO;
import com.admission.security.entity.SysUserFile;

import javax.servlet.http.HttpServletRequest;

/**
 *
 */
public interface SysUserFileService extends IService<SysUserFile> {
    Object findList(HttpServletRequest request, IPage<SysUserFile> page, FileRequestVO param);
}
