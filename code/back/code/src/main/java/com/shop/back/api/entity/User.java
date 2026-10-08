package com.shop.back.api.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 用户表
 * </p>
 *
 * @author @author

 */
@Data
@EqualsAndHashCode(callSuper = false)
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 用户表主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 用户表姓名
     */
    private String name;

    /**
     * 电话号码
     */
    private String phone;

    /**
     * 性别
     */
    private Integer sex;

    /**
     * 注册时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8") //Jackson包使用注解
    private Date time;

    /**
     * 0顾客1管理员
     */
    private Integer type;

    /**
     * 微信小程序唯一标识
     */
    @TableField("openId")
    private String openId;

    /**
     * 登录账号
     */
    private String number;

    /**
     * 登录密码
     */
    private String password;


    /**
     * 用户的头像
     */
    private String headimgUrl;


}
