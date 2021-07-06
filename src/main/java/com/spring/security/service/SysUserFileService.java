package com.spring.security.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.spring.security.entity.SysUserFile;

/**
 *
 */
public interface SysUserFileService extends IService<SysUserFile> {
    Object findList(IPage<SysUserFile> page);
}
