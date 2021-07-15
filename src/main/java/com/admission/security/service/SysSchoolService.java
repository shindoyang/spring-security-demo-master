package com.admission.security.service;

import com.admission.security.VO.SchoolRequestVO;
import com.admission.security.VO.SchoolUpdateRequestVO;
import com.admission.security.VO.UserRequestVO;
import com.admission.security.common.entity.JsonResult;
import com.admission.security.entity.SysSchool;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 *
 */
public interface SysSchoolService extends IService<SysSchool> {
    Object findList(IPage<SysSchool> page, SchoolRequestVO param);

    JsonResult addSchool(UserRequestVO param);

    JsonResult updateSchool(SchoolUpdateRequestVO param);
}
