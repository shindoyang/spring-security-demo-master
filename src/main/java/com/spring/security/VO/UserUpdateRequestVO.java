package com.spring.security.VO;

import lombok.Data;

@Data
public class UserUpdateRequestVO {

    //账号信息
    private Integer id;
    private String account;
    private String username;
    private String password;

}
