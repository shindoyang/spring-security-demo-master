package com.spring.security.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.spring.security.VO.SchoolRequestVO;
import com.spring.security.common.enums.ResultCode;
import com.spring.security.common.utils.ResultTool;
import com.spring.security.config.service.UserToolService;
import com.spring.security.dao.SysSchoolMapper;
import com.spring.security.entity.SysSchool;
import com.spring.security.service.SysSchoolService;
import com.spring.security.utils.DateUtils;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

/**
 *
 */
@Service
public class SysSchoolServiceImpl extends ServiceImpl<SysSchoolMapper, SysSchool>
        implements SysSchoolService {

    @Autowired
    UserToolService userToolService;

    @Override
    public Object findList(HttpServletRequest request, IPage<SysSchool> page, SchoolRequestVO param) {
        QueryWrapper<SysSchool> wrapper = new QueryWrapper<>();

        if (null != userToolService.getLoginUser(request)) {
            if ("admin".equals(userToolService.getLoginUser(request))) {
                return ResultTool.fail(ResultCode.NO_PERMISSION);
            }
        }
        if (null != param.getSchoolName() && Strings.isNotEmpty(param.getSchoolName()) && Strings.isNotBlank(param.getSchoolName())) {
            wrapper.like("school_name", param.getSchoolName());
        }
        if (null != param.getAccount() && Strings.isNotEmpty(param.getAccount()) && Strings.isNotBlank(param.getAccount())) {
            wrapper.like("account", param.getAccount());
        }
        if (param.getStartTime() != null && param.getEndTime() != null
                && !param.getStartTime().equals("undefined") && !param.getEndTime().equals("undefined")
                && Strings.isNotEmpty(param.getStartTime()) && Strings.isNotBlank(param.getStartTime())
                && Strings.isNotEmpty(param.getEndTime()) && Strings.isNotBlank(param.getEndTime())) {
            wrapper.between("create_time", DateUtils.getTimestamp(param.getStartTime()), DateUtils.getTimestamp(param.getEndTime()));
        }

        IPage<SysSchool> userIPage = baseMapper.selectPage(page, wrapper);
        return userIPage;
    }
}




