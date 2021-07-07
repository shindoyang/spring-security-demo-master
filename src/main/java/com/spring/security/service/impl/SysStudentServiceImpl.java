package com.spring.security.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.spring.security.config.service.UserToolService;
import com.spring.security.dao.SysStudentMapper;
import com.spring.security.entity.SysStudent;
import com.spring.security.service.SysStudentService;
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
    public Object findList(HttpServletRequest request, IPage<SysStudent> page) {
        QueryWrapper<SysStudent> wrapper = new QueryWrapper<>();

        if (null != userToolService.getLoginUser(request)) {
            wrapper.eq("account", userToolService.getLoginUser(request));
        }
        wrapper.eq("status", true);

        IPage<SysStudent> userIPage = baseMapper.selectPage(page, wrapper);
        return userIPage;
    }
}




