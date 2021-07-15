package com.admission.security.controller;

import com.admission.security.VO.*;
import com.admission.security.common.entity.JsonResult;
import com.admission.security.common.enums.ResultCode;
import com.admission.security.common.utils.ResultTool;
import com.admission.security.entity.SysSchool;
import com.admission.security.entity.SysStudent;
import com.admission.security.entity.SysUser;
import com.admission.security.service.SysSchoolService;
import com.admission.security.service.SysStudentService;
import com.admission.security.service.SysUserService;
import com.admission.security.utils.JWTUtils;
import com.admission.security.utils.MD5Util;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: Hutengfei
 * @Description:
 * @Date Create in 2019/8/28 19:34
 */
@Slf4j
@RestController
public class UserController {
    @Autowired
    SysUserService sysUserService;
    @Autowired
    SysStudentService sysStudentService;
    @Autowired
    SysSchoolService sysSchoolService;

    @PostMapping("/login")
    public JsonResult login(HttpServletResponse response, String username, String password) {
        //根据用户名查询用户
        SysUser sysUser = sysUserService.selectByName(username);
        if (sysUser == null) {
            return ResultTool.fail(ResultCode.USER_ACCOUNT_NOT_EXIST);
        }

        String reqPassword = MD5Util.getStringMD5("admission:" + password.trim());
        if (!sysUser.getPassword().equals(reqPassword)) {
            return ResultTool.fail(ResultCode.USER_CREDENTIALS_ERROR);
        }

        //用这个工具类生成token
        Map<String, String> map = new HashMap<>();
        map.put("username", sysUser.getAccount());
        String token = JWTUtils.generateTokenExpireInMinutes(map, 24 * 60);

        Map result = new HashMap<>();
        result.put("username", sysUser.getUserName());
        result.put("token", token);
        return ResultTool.success(result);
    }

    @GetMapping("/user/getUserByCode")
    public JsonResult test(String userCode) {

        if (null == userCode || Strings.isEmpty(userCode) || Strings.isBlank(userCode)) {
            return ResultTool.fail(ResultCode.PARAM_IS_BLANK);
        }

        QueryWrapper<SysStudent> stuWrapper = new QueryWrapper<>();
        stuWrapper.eq("stu_uid", userCode);
        stuWrapper.last("LIMIT 1");
        SysStudent one = sysStudentService.getOne(stuWrapper);
        SysStudentVO result = new SysStudentVO();
        BeanUtils.copyProperties(one, result);

        QueryWrapper<SysSchool> schWrapper = new QueryWrapper<>();
        schWrapper.eq("account", one.getAccount());
        schWrapper.last("LIMIT 1");
        SysSchool sch = sysSchoolService.getOne(schWrapper);
        result.setSchoolCode(sch.getSchoolCode());

        //第一次获取要更新状态，否则只更新次数
        if (null == one.getStatus() || !one.getStatus()) {
            one.setStatus(true);
            one.setClickTime(new Date());
            one.setClickNums(1);
            one.setUpdateTime(new Date());
        } else {
            one.setClickNums(one.getClickNums() + 1);
            one.setUpdateTime(new Date());
        }
        sysStudentService.updateById(one);
        return ResultTool.success(result);
    }

    //============================ 管理员接口 =============================

    /**
     * 获取所有账号信息
     */
    @GetMapping("/getUser")
    public JsonResult getUser() {
        List<SysUser> users = sysUserService.queryAllByLimit(1, 100);
        return ResultTool.success(users);
    }

    /**
     * 获取所有学校信息，可过滤
     */
    @GetMapping("/getSchool")
    public JsonResult getSchool(SchoolRequestVO param) {
        //获取前台发送过来的数据
        Integer pageNo = param.getPage();
        Integer pageSize = param.getSize();
        if (null == pageNo || null == pageSize) {
            return ResultTool.fail(ResultCode.PARAM_IS_BLANK);
        }

        IPage<SysSchool> page = new Page<>(pageNo, pageSize);
        Object list = sysSchoolService.findList(page, param);
        return ResultTool.success(list);
    }

    /**
     * 新建账号、学校信息
     */
    @PostMapping("/createUser")
    public JsonResult createUser(@RequestBody UserRequestVO param) {
        return sysSchoolService.addSchool(param);
    }

    /**
     * 更新账号信息
     */
    @PostMapping("/updateUser")
    public JsonResult updateUser(@RequestBody UserUpdateRequestVO param) {
        //参数校验
        if (null == param.getId()) {
            return ResultTool.fail(ResultCode.PARAM_IS_BLANK);
        }

        SysUser sysUser = sysUserService.queryById(param.getId());
        if (null == sysUser) {
            return ResultTool.fail(ResultCode.FAIL_NO_USER_ERROR);
        }

        if (null != param.getAccount() && Strings.isNotEmpty(param.getAccount()) && Strings.isNotBlank(param.getAccount())) {
            sysUser.setAccount(param.getAccount());
        }
        if (null != param.getUsername() && Strings.isNotEmpty(param.getUsername()) && Strings.isNotBlank(param.getUsername())) {
            sysUser.setUserName(param.getUsername());
        }
        if (null != param.getPassword() && Strings.isNotEmpty(param.getPassword()) && Strings.isNotBlank(param.getPassword())) {
            String password = MD5Util.getStringMD5("admission:" + param.getPassword().trim());
            sysUser.setPassword(password);
        }
        sysUser.setUpdateTime(new Date());
        sysUser.setUpdateUser(1);
        sysUserService.update(sysUser);
        return ResultTool.success();
    }

    /**
     * 更新学校信息
     */
    @PostMapping("/updateSchool")
    public JsonResult updateSchool(@RequestBody SchoolUpdateRequestVO param) {
        return sysSchoolService.updateSchool(param);
    }

}
