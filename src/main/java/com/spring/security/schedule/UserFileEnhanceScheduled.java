package com.spring.security.schedule;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.spring.security.config.service.UserToolService;
import com.spring.security.entity.SysUserFile;
import com.spring.security.service.SysUserFileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class UserFileEnhanceScheduled {

    @Autowired
    SysUserFileService sysUserFileService;

    @Autowired
    UserToolService userToolService;

    /**
     * 文件增强处理器
     */
    @Scheduled(cron = "${schedule.userfile.cron}")
    private void sendMessageScheduled() {
        log.info("sendMessageScheduled，开始查找待增强的文件");
        QueryWrapper<SysUserFile> wrapper = new QueryWrapper<>();
        wrapper.eq("status", 0);
        List<SysUserFile> list = sysUserFileService.list(wrapper);
        log.info("待处理文件数：{}", list.size());
        log.info("待处理文件内容：{}", JSON.toJSON(list));


    }
}
