package com.spring.security.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.spring.security.VO.SchoolRequestVO;
import com.spring.security.entity.SysSchool;

import javax.servlet.http.HttpServletRequest;

/**
 *
 */
public interface SysSchoolService extends IService<SysSchool> {
    Object findList(HttpServletRequest request, IPage<SysSchool> page, SchoolRequestVO param);
}
