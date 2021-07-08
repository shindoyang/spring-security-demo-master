package com.spring.security.VO;

import lombok.Data;

@Data
public class LinkRequestVO {
    private Integer page;

    private Integer size;

    private String mobile;

    private String startTime;

    private String endTime;

}
