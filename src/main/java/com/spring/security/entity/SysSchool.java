package com.spring.security.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;

/**
 * 
 * @TableName sys_school
 */
@TableName(value ="sys_school")
public class SysSchool implements Serializable {
    /**
     * 主键id
     */
    @TableId(type = IdType.AUTO)
    private Long id;

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

    /**
     * 
     */
    private Date createTime;

    /**
     * 
     */
    private Date updateTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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

    /**
     * 
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * 
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * 
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * 
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
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
            && (this.getHost() == null ? other.getHost() == null : this.getHost().equals(other.getHost()))
            && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(other.getCreateTime()))
            && (this.getUpdateTime() == null ? other.getUpdateTime() == null : this.getUpdateTime().equals(other.getUpdateTime()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getSchoolName() == null) ? 0 : getSchoolName().hashCode());
        result = prime * result + ((getAccount() == null) ? 0 : getAccount().hashCode());
        result = prime * result + ((getHost() == null) ? 0 : getHost().hashCode());
        result = prime * result + ((getCreateTime() == null) ? 0 : getCreateTime().hashCode());
        result = prime * result + ((getUpdateTime() == null) ? 0 : getUpdateTime().hashCode());
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
        sb.append(", createTime=").append(createTime);
        sb.append(", updateTime=").append(updateTime);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}