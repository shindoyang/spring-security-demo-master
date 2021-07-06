package com.spring.security.entity;

import lombok.Data;

import java.util.Date;

@Data
//@ApiModel(value = "消息发送请求参数", description = "消息发送请求参数")
public class FileUploadEntity {
    //    @ApiModelProperty(value = "消息模板ID")
    private String fileName;

    private Integer page;

    private Integer size;

    private Date startTime;

    private Date endTime;

}
