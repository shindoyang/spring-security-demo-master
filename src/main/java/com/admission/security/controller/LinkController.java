package com.admission.security.controller;

import com.admission.security.VO.LinkRequestVO;
import com.admission.security.common.entity.JsonResult;
import com.admission.security.common.enums.ResultCode;
import com.admission.security.common.utils.ResultTool;
import com.admission.security.config.service.UserToolService;
import com.admission.security.entity.SysStudent;
import com.admission.security.service.SysStudentService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@Slf4j
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
        log.info("当前用户：{}，请求接口：{}", userToolService.getLoginUser(request), "/link/page 已访问链接统计接口");

        //获取前台发送过来的数据
        Integer pageNo = param.getPage();
        Integer pageSize = param.getSize();
        if (null == pageNo || null == pageSize) {
            return ResultTool.fail(ResultCode.PARAM_IS_BLANK);
        }

        IPage<SysStudent> page = new Page<>(pageNo, pageSize);
        Object list = sysStudentService.findList(request, page, param);
        return ResultTool.success(list);
    }
}
