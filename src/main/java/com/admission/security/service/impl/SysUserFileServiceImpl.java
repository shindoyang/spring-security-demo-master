package com.admission.security.service.impl;

import com.admission.security.VO.FileRequestVO;
import com.admission.security.entity.SysUserFile;
import com.admission.security.mapper.SysUserFileMapper;
import com.admission.security.service.SysUserFileService;
import com.admission.security.utils.DateUtils;
import com.admission.security.utils.SecurityUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

/**
 *
 */
@Service
public class SysUserFileServiceImpl extends ServiceImpl<SysUserFileMapper, SysUserFile>
        implements SysUserFileService {

    @Override
    public Object findList(HttpServletRequest request, IPage<SysUserFile> page, FileRequestVO param) {
        QueryWrapper<SysUserFile> wrapper = new QueryWrapper<>();

        wrapper.eq("account", SecurityUtils.getUsername());
        if (null != param.getFileName() && Strings.isNotEmpty(param.getFileName()) && Strings.isNotBlank(param.getFileName())) {
            wrapper.like("file_name", param.getFileName());
        }
        if (param.getStartTime() != null && param.getEndTime() != null
                && !param.getStartTime().equals("undefined") && !param.getEndTime().equals("undefined")
                && Strings.isNotEmpty(param.getStartTime()) && Strings.isNotBlank(param.getStartTime())
                && Strings.isNotEmpty(param.getEndTime()) && Strings.isNotBlank(param.getEndTime())) {
            wrapper.between("create_time", DateUtils.getTimestamp(param.getStartTime()), DateUtils.getTimestamp(param.getEndTime()));
        }
        wrapper.orderByDesc("create_time");

        IPage<SysUserFile> userIPage = baseMapper.selectPage(page, wrapper);
        if (userIPage.getRecords().size() > 0) {

        }
        return userIPage;
    }
}




