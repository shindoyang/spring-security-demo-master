package com.spring.security.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.spring.security.entity.SysStudent;

import javax.servlet.http.HttpServletRequest;

/**
 *
 */
public interface SysStudentService extends IService<SysStudent> {
    Object findList(HttpServletRequest request, IPage<SysStudent> page);
}
