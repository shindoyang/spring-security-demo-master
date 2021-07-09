package com.admission.security.entity;

import java.io.Serializable;
import lombok.Data;

/**
 * sys_user_school_relation
 * @author 
 */
@Data
public class SysUserSchoolRelation implements Serializable {
    /**
     * 主键id
     */
    private Integer id;

    /**
     * 用户id
     */
    private Integer userId;

    /**
     * 学校id
     */
    private Integer schoolId;

    private static final long serialVersionUID = 1L;
}