package com.admission.security.service;

import com.admission.security.VO.LinkRequestVO;
import com.admission.security.entity.SysStudent;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 *
 */
public interface SysStudentService extends IService<SysStudent> {
    Object findList(IPage<SysStudent> page, LinkRequestVO param);
}
