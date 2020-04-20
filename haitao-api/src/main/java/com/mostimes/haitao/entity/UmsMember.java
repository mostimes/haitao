package com.mostimes.haitao.entity;

import lombok.Data;

import java.util.Date;

@Data
public class UmsMember {
    private String id;                       //id
    private String password;                 //登录密码
    private String payPassword;             //支付密码
    private String name;                     //真实姓名
    private String nickname;                 //昵称
    private String phone;                    //手机号码
    private String email;                    //邮箱地址
    private String status;                   //帐号启用状态:0->禁用；1->启用
    private Date createTime;                //注册时间
    private String icon;                     //头像
    private String gender;                   //性别：0->未知；1->男；2->女
    private String birthday;                   //生日
    private String city;                     //所在城市
    private String job;                      //职业
    private String personalizedSignature;   //个性签名
    private String token;                    //令牌
}
