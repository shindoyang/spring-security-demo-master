package com.admission.security.VO;

import lombok.Data;

@Data
public class UserRequestVO {

    //账号信息
    private String account;
    private String username;
    private String password;

    //学校信息
    private String schoolName;
    private String host;

}
