package com.spring.security.VO;

import lombok.Data;

@Data
public class FileRequestVO {
    private String fileId;

    private String fileName;

    private Integer page;

    private Integer size;

    private String startTime;

    private String endTime;

}
