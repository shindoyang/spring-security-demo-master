package com.spring.security.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.spring.security.config.service.UserToolService;
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
//    @Autowired
//    SecurityContextService securityContextService;

    @Autowired
    UserToolService userToolService;

    @Override
    public Object findList(IPage<SysUserFile> page) {
        QueryWrapper<SysUserFile> wrapper = new QueryWrapper<>();

        /*if (null != userToolService.getLoginUser()) {
            wrapper.eq("account", userToolService.getLoginUser());
        }*/
        IPage<SysUserFile> userIPage = baseMapper.selectPage(page, wrapper);
        return userIPage;
    }
}




