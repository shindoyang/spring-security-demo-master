package com.admission.security.service;

import com.admission.security.VO.LinkRequestVO;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.admission.security.entity.SysStudent;

import javax.servlet.http.HttpServletRequest;

/**
 *
 */
public interface SysStudentService extends IService<SysStudent> {
    Object findList(HttpServletRequest request, IPage<SysStudent> page, LinkRequestVO param);
}
