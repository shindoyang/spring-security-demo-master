package com.spring.security.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;

/**
 * 
 * @TableName sys_school
 */
@TableName(value ="sys_school")
public class SysSchool implements Serializable {
    /**
     * 主键id
     */
    @TableId
    private Integer id;

    /**
     * 学校名
     */
    private String schoolName;

    /**
     * 关联账号
     */
    private String account;

    /**
     * 录取通知书访问路径前缀
     */
    private String host;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    public Integer getId() {
        return id;
    }

    /**
     * 主键id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 学校名
     */
    public String getSchoolName() {
        return schoolName;
    }

    /**
     * 学校名
     */
    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }

    /**
     * 关联账号
     */
    public String getAccount() {
        return account;
    }

    /**
     * 关联账号
     */
    public void setAccount(String account) {
        this.account = account;
    }

    /**
     * 录取通知书访问路径前缀
     */
    public String getHost() {
        return host;
    }

    /**
     * 录取通知书访问路径前缀
     */
    public void setHost(String host) {
        this.host = host;
    }

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        SysSchool other = (SysSchool) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getSchoolName() == null ? other.getSchoolName() == null : this.getSchoolName().equals(other.getSchoolName()))
            && (this.getAccount() == null ? other.getAccount() == null : this.getAccount().equals(other.getAccount()))
            && (this.getHost() == null ? other.getHost() == null : this.getHost().equals(other.getHost()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getSchoolName() == null) ? 0 : getSchoolName().hashCode());
        result = prime * result + ((getAccount() == null) ? 0 : getAccount().hashCode());
        result = prime * result + ((getHost() == null) ? 0 : getHost().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", schoolName=").append(schoolName);
        sb.append(", account=").append(account);
        sb.append(", host=").append(host);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}