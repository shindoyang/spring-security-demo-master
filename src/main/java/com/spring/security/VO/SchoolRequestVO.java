package com.spring.security.VO;

import lombok.Data;

@Data
public class SchoolRequestVO {
    private Integer page;

    private Integer size;

    private String schoolName;

    private String account;

    private String startTime;

    private String endTime;

}
