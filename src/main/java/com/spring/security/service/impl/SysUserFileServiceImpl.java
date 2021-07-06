package com.spring.security.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.spring.security.common.enums.ResultCode;
import com.spring.security.common.utils.ResultTool;
import com.spring.security.config.service.SecurityContextService;
import com.spring.security.dao.SysUserFileMapper;
import com.spring.security.entity.SysUserFile;
import com.spring.security.service.SysUserFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 */
@Service
public class SysUserFileServiceImpl extends ServiceImpl<SysUserFileMapper, SysUserFile>
        implements SysUserFileService {
    @Autowired
    SecurityContextService securityContextService;

    @Override
    public Object findList(IPage<SysUserFile> page) {
        QueryWrapper<SysUserFile> wrapper = new QueryWrapper<>();
        //1、检查是否已登录
        String account = securityContextService.getLoginUserName();
        if (null == account) {
            return ResultTool.fail(ResultCode.USER_NOT_LOGIN);
        }
        if (null != account) {
            wrapper.eq("account", account);
        }
        IPage<SysUserFile> userIPage = baseMapper.selectPage(page, wrapper);
        return userIPage;
    }
}




