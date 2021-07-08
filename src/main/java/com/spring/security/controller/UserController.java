package com.spring.security.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.spring.security.VO.SchoolRequestVO;
import com.spring.security.VO.SchoolUpdateRequestVO;
import com.spring.security.VO.UserRequestVO;
import com.spring.security.common.entity.JsonResult;
import com.spring.security.common.enums.ResultCode;
import com.spring.security.common.utils.ResultTool;
import com.spring.security.config.service.UserToolService;
import com.spring.security.entity.SysSchool;
import com.spring.security.entity.SysStudent;
import com.spring.security.entity.SysUser;
import com.spring.security.service.SysSchoolService;
import com.spring.security.service.SysStudentService;
import com.spring.security.service.SysUserService;
import com.spring.security.utils.JWTUtils;
import com.spring.security.utils.MD5Util;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
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
@RestController
public class UserController {
    @Autowired
    SysUserService sysUserService;
    @Autowired
    SysStudentService sysStudentService;
    @Autowired
    SysSchoolService sysSchoolService;
    @Autowired
    UserToolService userToolService;

    @PostMapping("/login")
    public JsonResult login(HttpServletResponse response, String username, String password) {
        //根据用户名查询用户
        SysUser sysUser = sysUserService.selectByName(username);
        if (sysUser == null) {
            return ResultTool.fail(ResultCode.USER_ACCOUNT_NOT_EXIST);
        }

        String reqPassword = MD5Util.getStringMD5("admission:" + password);
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
        return ResultTool.success(one);
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
    public JsonResult getSchool(HttpServletRequest request, SchoolRequestVO param) {
        //1、检查是否已登录
        if (!userToolService.checkUserlogin(request)) {
            return ResultTool.fail(ResultCode.USER_NOT_LOGIN);
        }

        //获取前台发送过来的数据
        Integer pageNo = param.getPage();
        Integer pageSize = param.getSize();
        if (null == pageNo || null == pageSize) {
            return ResultTool.fail(ResultCode.PARAM_IS_BLANK);
        }

        IPage<SysSchool> page = new Page<>(pageNo, pageSize);
        Object list = sysSchoolService.findList(request, page, param);
        return ResultTool.success(list);
    }

    /**
     * 新建账号、学校信息
     */
    @PostMapping("/createUser")
    public JsonResult createUser(HttpServletRequest request, @RequestBody UserRequestVO param) {
        //1、检查是否已登录
        if (!userToolService.checkUserlogin(request)) {
            return ResultTool.fail(ResultCode.USER_NOT_LOGIN);
        }

        return sysSchoolService.addSchool(request, param);
    }

    /**
     * 更新学校信息
     */
    @PostMapping("/updateSchool")
    public JsonResult updateSchool(HttpServletRequest request, @RequestBody SchoolUpdateRequestVO param) {
        //1、检查是否已登录
        if (!userToolService.checkUserlogin(request)) {
            return ResultTool.fail(ResultCode.USER_NOT_LOGIN);
        }

        return sysSchoolService.updateSchool(request, param);
    }

}
