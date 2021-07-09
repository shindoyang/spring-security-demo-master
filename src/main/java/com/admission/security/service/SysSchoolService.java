package com.admission.security.service;

import com.admission.security.VO.SchoolRequestVO;
import com.admission.security.VO.UserRequestVO;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.admission.security.VO.SchoolUpdateRequestVO;
import com.admission.security.common.entity.JsonResult;
import com.admission.security.entity.SysSchool;

import javax.servlet.http.HttpServletRequest;

/**
 *
 */
public interface SysSchoolService extends IService<SysSchool> {
    Object findList(HttpServletRequest request, IPage<SysSchool> page, SchoolRequestVO param);

    JsonResult addSchool(HttpServletRequest request, UserRequestVO param);

    JsonResult updateSchool(HttpServletRequest request, SchoolUpdateRequestVO param);
}
