package com.spring.security.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * @TableName sys_student
 */
@Data
@TableName(value = "sys_student")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class SysStudent implements Serializable {
    /**
     *
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 用户账号
     */
    private String account;

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
     * 第一次访问时间
     */
    private Date clickTime;

    /**
     * 点击次数
     */
    private Integer clickNums;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间，每次点击都更新
     */
    private Date updateTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

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
        SysStudent other = (SysStudent) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
                && (this.getAccount() == null ? other.getAccount() == null : this.getAccount().equals(other.getAccount()))
                && (this.getMobile() == null ? other.getMobile() == null : this.getMobile().equals(other.getMobile()))
                && (this.getText1() == null ? other.getText1() == null : this.getText1().equals(other.getText1()))
                && (this.getText2() == null ? other.getText2() == null : this.getText2().equals(other.getText2()))
                && (this.getText3() == null ? other.getText3() == null : this.getText3().equals(other.getText3()))
                && (this.getText4() == null ? other.getText4() == null : this.getText4().equals(other.getText4()))
                && (this.getText5() == null ? other.getText5() == null : this.getText5().equals(other.getText5()))
                && (this.getText6() == null ? other.getText6() == null : this.getText6().equals(other.getText6()))
                && (this.getText7() == null ? other.getText7() == null : this.getText7().equals(other.getText7()))
                && (this.getText8() == null ? other.getText8() == null : this.getText8().equals(other.getText8()))
                && (this.getText9() == null ? other.getText9() == null : this.getText9().equals(other.getText9()))
                && (this.getText10() == null ? other.getText10() == null : this.getText10().equals(other.getText10()))
                && (this.getClickTime() == null ? other.getClickTime() == null : this.getClickTime().equals(other.getClickTime()))
                && (this.getClickNums() == null ? other.getClickNums() == null : this.getClickNums().equals(other.getClickNums()))
                && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(other.getCreateTime()))
                && (this.getUpdateTime() == null ? other.getUpdateTime() == null : this.getUpdateTime().equals(other.getUpdateTime()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getAccount() == null) ? 0 : getAccount().hashCode());
        result = prime * result + ((getMobile() == null) ? 0 : getMobile().hashCode());
        result = prime * result + ((getText1() == null) ? 0 : getText1().hashCode());
        result = prime * result + ((getText2() == null) ? 0 : getText2().hashCode());
        result = prime * result + ((getText3() == null) ? 0 : getText3().hashCode());
        result = prime * result + ((getText4() == null) ? 0 : getText4().hashCode());
        result = prime * result + ((getText5() == null) ? 0 : getText5().hashCode());
        result = prime * result + ((getText6() == null) ? 0 : getText6().hashCode());
        result = prime * result + ((getText7() == null) ? 0 : getText7().hashCode());
        result = prime * result + ((getText8() == null) ? 0 : getText8().hashCode());
        result = prime * result + ((getText9() == null) ? 0 : getText9().hashCode());
        result = prime * result + ((getText10() == null) ? 0 : getText10().hashCode());
        result = prime * result + ((getClickTime() == null) ? 0 : getClickTime().hashCode());
        result = prime * result + ((getClickNums() == null) ? 0 : getClickNums().hashCode());
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
        sb.append(", account=").append(account);
        sb.append(", mobile=").append(mobile);
        sb.append(", text1=").append(text1);
        sb.append(", text2=").append(text2);
        sb.append(", text3=").append(text3);
        sb.append(", text4=").append(text4);
        sb.append(", text5=").append(text5);
        sb.append(", text6=").append(text6);
        sb.append(", text7=").append(text7);
        sb.append(", text8=").append(text8);
        sb.append(", text9=").append(text9);
        sb.append(", text10=").append(text10);
        sb.append(", clickTime=").append(clickTime);
        sb.append(", clickNums=").append(clickNums);
        sb.append(", createTime=").append(createTime);
        sb.append(", updateTime=").append(updateTime);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}