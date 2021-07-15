package com.admission.security.service.impl;

import com.admission.security.VO.SchoolRequestVO;
import com.admission.security.VO.SchoolUpdateRequestVO;
import com.admission.security.VO.UserRequestVO;
import com.admission.security.common.entity.JsonResult;
import com.admission.security.common.enums.ResultCode;
import com.admission.security.common.utils.ResultTool;
import com.admission.security.entity.SysSchool;
import com.admission.security.entity.SysUser;
import com.admission.security.entity.SysUserRoleRelation;
import com.admission.security.mapper.SysSchoolMapper;
import com.admission.security.service.SysSchoolService;
import com.admission.security.service.SysUserRoleRelationService;
import com.admission.security.service.SysUserService;
import com.admission.security.utils.DateUtils;
import com.admission.security.utils.SecurityUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 *
 */
@Service
public class SysSchoolServiceImpl extends ServiceImpl<SysSchoolMapper, SysSchool>
        implements SysSchoolService {
    @Autowired
    SysUserService sysUserService;
    @Autowired
    SysSchoolService sysSchoolService;
    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    SysUserRoleRelationService sysUserRoleRelationService;

    @Override
    public Object findList(IPage<SysSchool> page, SchoolRequestVO param) {
        QueryWrapper<SysSchool> wrapper = new QueryWrapper<>();

        if (!"admin".equals(SecurityUtils.getUsername())) {
            return ResultTool.fail(ResultCode.NO_PERMISSION);
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
    public JsonResult addSchool(UserRequestVO param) {
        if (!"admin".equals(SecurityUtils.getUsername())) {
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
        wrapper.eq("school_code", param.getSchoolCode());
        wrapper.eq("school_name", param.getSchoolName());
        SysSchool dbSysSchool = sysSchoolService.getOne(wrapper);
        if (null != dbSysSchool) {
            return ResultTool.fail(ResultCode.FAIL_SCHOOL_REPEAT_ERROR);
        }

        //新增账号
        SysUser sysUser = new SysUser();
        sysUser.setAccount(param.getAccount());
        sysUser.setUserName(param.getUsername());
//        String dbPassword = MD5Util.getStringMD5("admission:" + param.getPassword().trim());
        String dbPassword = bCryptPasswordEncoder.encode(param.getPassword().trim());
        sysUser.setPassword(dbPassword);
        sysUser.setEnabled(true);
        sysUser.setAccountNonExpired(true);
        sysUser.setAccountNonExpired(true);
        sysUser.setAccountNonLocked(true);
        sysUser.setCredentialsNonExpired(true);
        sysUser.setCreateTime(new Date());
        sysUser.setCreateUser(1);
        SysUser insert = sysUserService.insert(sysUser);

        //新增用户权限
        SysUserRoleRelation sysUserRoleRelation = new SysUserRoleRelation();
        sysUserRoleRelation.setUserId(insert.getId());
        sysUserRoleRelation.setRoleId(2);
        sysUserRoleRelationService.save(sysUserRoleRelation);

        //新增学校
        SysSchool sysSchool = new SysSchool();
        sysSchool.setAccount(param.getAccount());
        sysSchool.setSchoolCode(param.getSchoolCode());
        sysSchool.setSchoolName(param.getSchoolName());
        sysSchool.setHost(param.getHost());
        sysSchool.setCreateTime(new Date());
        sysSchoolService.save(sysSchool);

        return ResultTool.success();
    }

    @Override
    public JsonResult updateSchool(SchoolUpdateRequestVO param) {
        if (!"admin".equals(SecurityUtils.getUsername())) {
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
        if (null != param.getSchoolCode() && Strings.isNotEmpty(param.getSchoolCode()) && Strings.isNotBlank(param.getSchoolCode())) {
            dbSysSchool.setSchoolCode(param.getSchoolCode());
        }
        if (null != param.getHost() && Strings.isNotEmpty(param.getHost()) && Strings.isNotBlank(param.getHost())) {
            dbSysSchool.setHost(param.getHost());
        }
        dbSysSchool.setUpdateTime(new Date());
        sysSchoolService.updateById(dbSysSchool);

        return ResultTool.success();
    }
}




