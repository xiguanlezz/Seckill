package com.cj.cn.pojo;

import java.util.Date;
import javax.persistence.*;

import lombok.*;
import lombok.experimental.Accessors;

@Accessors(chain = true)
@Getter
@Setter
@Table(name = "user")
public class User {
    /**
     * 用户ID, 手机号码
     */
    @Id
    private Long id;

    private String nickname;

    /**
     * MD5(MD5(明文密码+固定salt),salt)
     */
    private String password;

    private String salt;

    /**
     * 头像, 云存储的地址
     */
    private String head;

    /**
     * 注册时间
     */
    @Column(name = "register_date")
    private Date registerDate;

    /**
     * 上次登录时间
     */
    @Column(name = "last_login_date")
    private Date lastLoginDate;

    /**
     * 登录次数
     */
    @Column(name = "login_count")
    private Integer loginCount;
}