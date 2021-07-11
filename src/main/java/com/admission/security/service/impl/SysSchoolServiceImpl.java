package com.admission.security.service.impl;

import com.admission.security.VO.SchoolRequestVO;
import com.admission.security.VO.UserRequestVO;
import com.admission.security.common.entity.JsonResult;
import com.admission.security.common.enums.ResultCode;
import com.admission.security.common.utils.ResultTool;
import com.admission.security.config.service.UserToolService;
import com.admission.security.entity.SysSchool;
import com.admission.security.service.SysSchoolService;
import com.admission.security.service.SysUserService;
import com.admission.security.utils.DateUtils;
import com.admission.security.utils.MD5Util;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.admission.security.VO.SchoolUpdateRequestVO;
import com.admission.security.dao.SysSchoolMapper;
import com.admission.security.entity.SysUser;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 *
 */
@Service
public class SysSchoolServiceImpl extends ServiceImpl<SysSchoolMapper, SysSchool>
        implements SysSchoolService {

    @Autowired
    UserToolService userToolService;
    @Autowired
    SysUserService sysUserService;
    @Autowired
    SysSchoolService sysSchoolService;

    @Override
    public Object findList(HttpServletRequest request, IPage<SysSchool> page, SchoolRequestVO param) {
        QueryWrapper<SysSchool> wrapper = new QueryWrapper<>();

        if (null != userToolService.getLoginUser(request)) {
            if (!"admin".equals(userToolService.getLoginUser(request))) {
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

    @Override
    @Transactional
    public JsonResult addSchool(HttpServletRequest request, UserRequestVO param) {
        if (!"admin".equals(userToolService.getLoginUser(request))) {
            return ResultTool.fail(ResultCode.NO_PERMISSION);
        }

        //参数校验
        if (null == param.getAccount() || null == param.getUsername() || null == param.getPassword()
                || null == param.getSchoolName() || null == param.getHost()) {
            return ResultTool.fail(ResultCode.PARAM_IS_BLANK);
        }

        //判断用户是否重复注册
        SysUser dbSysUser = sysUserService.selectByName(param.getAccount());
        if (null != dbSysUser) {
            return ResultTool.fail(ResultCode.FAIL_USER_REPEAT_ERROR);
        }
        //判断学校是否重复注册
        QueryWrapper<SysSchool> wrapper = new QueryWrapper<>();
        wrapper.eq("school_name", param.getSchoolName());
        SysSchool dbSysSchool = sysSchoolService.getOne(wrapper);
        if (null != dbSysSchool) {
            return ResultTool.fail(ResultCode.FAIL_SCHOOL_REPEAT_ERROR);
        }

        //新增账号
        SysUser sysUser = new SysUser();
        sysUser.setAccount(param.getAccount());
        sysUser.setUserName(param.getUsername());
        String dbPassword = MD5Util.getStringMD5("admission:" + param.getPassword().trim());
        sysUser.setPassword(dbPassword);
        sysUser.setEnabled(true);
        sysUser.setAccountNonExpired(true);
        sysUser.setAccountNonExpired(true);
        sysUser.setAccountNonLocked(true);
        sysUser.setCredentialsNonExpired(true);
        sysUser.setCreateTime(new Date());
        sysUser.setCreateUser(1);
        sysUserService.insert(sysUser);

        //新增学校
        SysSchool sysSchool = new SysSchool();
        sysSchool.setAccount(param.getAccount());
        sysSchool.setSchoolName(param.getSchoolName());
        sysSchool.setHost(param.getHost());
        sysSchool.setCreateTime(new Date());
        sysSchoolService.save(sysSchool);

        return ResultTool.success();
    }

    @Override
    public JsonResult updateSchool(HttpServletRequest request, SchoolUpdateRequestVO param) {
        if (!"admin".equals(userToolService.getLoginUser(request))) {
            return ResultTool.fail(ResultCode.NO_PERMISSION);
        }

        //参数校验
        if (null == param.getId()) {
            return ResultTool.fail(ResultCode.PARAM_IS_BLANK);
        }

        QueryWrapper<SysSchool> wrapper = new QueryWrapper<>();
        wrapper.eq("id", param.getId());
        SysSchool dbSysSchool = sysSchoolService.getOne(wrapper);
        if (null == dbSysSchool) {
            return ResultTool.fail(ResultCode.FAIL_NO_SCHOOL_ERROR);
        }

        if (null != param.getAccount() && Strings.isNotEmpty(param.getAccount()) && Strings.isNotBlank(param.getAccount())) {
            dbSysSchool.setAccount(param.getAccount());
        }
        if (null != param.getSchoolName() && Strings.isNotEmpty(param.getSchoolName()) && Strings.isNotBlank(param.getSchoolName())) {
            dbSysSchool.setSchoolName(param.getSchoolName());
        }
        if (null != param.getHost() && Strings.isNotEmpty(param.getHost()) && Strings.isNotBlank(param.getHost())) {
            dbSysSchool.setHost(param.getHost());
        }
        dbSysSchool.setUpdateTime(new Date());
        sysSchoolService.updateById(dbSysSchool);

        return ResultTool.success();
    }
}



