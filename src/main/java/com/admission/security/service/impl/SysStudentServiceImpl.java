package com.admission.security.service.impl;

import com.admission.security.VO.LinkRequestVO;
import com.admission.security.common.enums.ResultCode;
import com.admission.security.common.utils.ResultTool;
import com.admission.security.dao.SysStudentMapper;
import com.admission.security.entity.SysStudent;
import com.admission.security.service.SysStudentService;
import com.admission.security.utils.DateUtils;
import com.admission.security.utils.MobileUtil;
import com.admission.security.utils.SecurityUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Service;

/**
 *
 */
@Service
public class SysStudentServiceImpl extends ServiceImpl<SysStudentMapper, SysStudent>
        implements SysStudentService {

    @Override
    public Object findList(IPage<SysStudent> page, LinkRequestVO param) {
        QueryWrapper<SysStudent> wrapper = new QueryWrapper<>();

        wrapper.eq("account", SecurityUtils.getUsername());
        if (null != param.getMobile() && Strings.isNotEmpty(param.getMobile()) && Strings.isNotBlank(param.getMobile())) {
            if (!MobileUtil.isMobileNO(param.getMobile())) {
                return ResultTool.fail(ResultCode.FAIL_MOBILE_ERROR);
            }
            wrapper.eq("mobile", param.getMobile());
        }
        if (param.getStartTime() != null && param.getEndTime() != null
                && !param.getStartTime().equals("undefined") && !param.getEndTime().equals("undefined")
                && Strings.isNotEmpty(param.getStartTime()) && Strings.isNotBlank(param.getStartTime())
                && Strings.isNotEmpty(param.getEndTime()) && Strings.isNotBlank(param.getEndTime())) {
            wrapper.between("click_time", DateUtils.getTimestamp(param.getStartTime()), DateUtils.getTimestamp(param.getEndTime()));
        }
        wrapper.eq("status", true);
        wrapper.orderByDesc("click_time");

        IPage<SysStudent> userIPage = baseMapper.selectPage(page, wrapper);
        return userIPage;
    }
}




