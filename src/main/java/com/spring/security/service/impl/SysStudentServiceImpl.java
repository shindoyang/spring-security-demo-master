package com.spring.security.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.spring.security.VO.LinkRequestVO;
import com.spring.security.common.enums.ResultCode;
import com.spring.security.common.utils.ResultTool;
import com.spring.security.config.service.UserToolService;
import com.spring.security.dao.SysStudentMapper;
import com.spring.security.entity.SysStudent;
import com.spring.security.service.SysStudentService;
import com.spring.security.utils.DateUtils;
import com.spring.security.utils.MobileUtil;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

/**
 *
 */
@Service
public class SysStudentServiceImpl extends ServiceImpl<SysStudentMapper, SysStudent>
        implements SysStudentService {

    @Autowired
    UserToolService userToolService;

    @Override
    public Object findList(HttpServletRequest request, IPage<SysStudent> page, LinkRequestVO param) {
        QueryWrapper<SysStudent> wrapper = new QueryWrapper<>();

        if (null != userToolService.getLoginUser(request)) {
            wrapper.eq("account", userToolService.getLoginUser(request));
        }

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




