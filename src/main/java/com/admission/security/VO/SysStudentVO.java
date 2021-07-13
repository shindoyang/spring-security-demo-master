package com.admission.security.VO;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
public class SysStudentVO {

    /**
     * 学校代号
     */
    private String schoolCode;

    /**
     * 用户账号
     */
    private String account;

    private String stuUid;

    /**
     * 手机号
     */
    private String mobile;

    /**
     * 参数1
     */
    private String text1;

    /**
     * 参数2
     */
    private String text2;

    /**
     * 参数3
     */
    private String text3;

    /**
     * 参数4
     */
    private String text4;

    /**
     * 参数5
     */
    private String text5;

    /**
     * 参数6
     */
    private String text6;

    /**
     * 参数7
     */
    private String text7;

    /**
     * 参数8
     */
    private String text8;

    /**
     * 参数9
     */
    private String text9;

    /**
     * 参数10
     */
    private String text10;

    /**
     * 是否点击 0：未点击，1：已点击
     */
    private Boolean status;

    /**
     * 第一次访问时间
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date clickTime;

    /**
     * 点击次数
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Integer clickNums;

    /**
     * 创建时间
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 更新时间，每次点击都更新
     */
    private Date updateTime;
}