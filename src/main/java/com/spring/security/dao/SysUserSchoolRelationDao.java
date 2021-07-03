package com.spring.security.dao;

import com.spring.security.entity.SysUserSchoolRelation;

public interface SysUserSchoolRelationDao {
    int deleteByPrimaryKey(Integer id);

    int insert(SysUserSchoolRelation record);

    int insertSelective(SysUserSchoolRelation record);

    SysUserSchoolRelation selectByPrimaryKey(Integer id);

    SysUserSchoolRelation queryByUsername(String userName);

    int updateByPrimaryKeySelective(SysUserSchoolRelation record);

    int updateByPrimaryKey(SysUserSchoolRelation record);
}