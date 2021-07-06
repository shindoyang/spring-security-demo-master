package com.spring.security.VO;

import lombok.Data;

import java.util.Date;

@Data
public class FileRequestVO {
    private String fileId;

    private String fileName;

    private Integer page;

    private Integer size;

    private Date startTime;

    private Date endTime;

}
