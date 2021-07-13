package com.admission.security.VO;

import lombok.Data;

@Data
public class SchoolUpdateRequestVO {

    //学校信息
    private Long id;
    private String account;
    private String schoolCode;
    private String schoolName;
    private String host;

}
