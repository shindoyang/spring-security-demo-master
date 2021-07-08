package com.spring.security.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.spring.security.VO.LinkRequestVO;
import com.spring.security.common.entity.JsonResult;
import com.spring.security.common.enums.ResultCode;
import com.spring.security.common.utils.ResultTool;
import com.spring.security.config.service.UserToolService;
import com.spring.security.entity.SysStudent;
import com.spring.security.service.SysStudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/link")
public class LinkController {

    @Autowired
    UserToolService userToolService;

    @Autowired
    SysStudentService sysStudentService;

    @GetMapping("/page")
    public JsonResult findAll(HttpServletRequest request, LinkRequestVO param) {
        //1、检查是否已登录
        if (!userToolService.checkUserlogin(request)) {
            return ResultTool.fail(ResultCode.USER_NOT_LOGIN);
        }
        String loginUser = userToolService.getLoginUser(request);

        //获取前台发送过来的数据
        Integer pageNo = param.getPage();
        Integer pageSize = param.getSize();
        IPage<SysStudent> page = new Page<>(pageNo, pageSize);
        Object list = sysStudentService.findList(request, page, param);
        if (null != list) {
            return ResultTool.success(list);
        }
        return ResultTool.success();
    }
}
