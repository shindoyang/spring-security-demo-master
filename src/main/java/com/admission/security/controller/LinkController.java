package com.admission.security.controller;

import com.admission.security.VO.LinkRequestVO;
import com.admission.security.common.entity.JsonResult;
import com.admission.security.common.enums.ResultCode;
import com.admission.security.common.utils.ResultTool;
import com.admission.security.entity.SysStudent;
import com.admission.security.service.SysStudentService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/link")
public class LinkController {

    @Autowired
    SysStudentService sysStudentService;

    @PostMapping("/page")
    public JsonResult findAll(@RequestBody LinkRequestVO param) {
        //获取前台发送过来的数据
        Integer pageNo = param.getPage();
        Integer pageSize = param.getSize();
        if (null == pageNo || null == pageSize) {
            return ResultTool.fail(ResultCode.PARAM_IS_BLANK);
        }

        IPage<SysStudent> page = new Page<>(pageNo, pageSize);
        Object list = sysStudentService.findList(page, param);
        return ResultTool.success(list);
    }
}
