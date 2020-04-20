package com.mostimes.haitao.entity;

import lombok.Data;

@Data
public class UmsMemberReceiveAddress {
    private String id;              //id
    private String memberId;          //会员id
    private String name;            //收货人名称
    private String phoneNumber;     //收货人电话
    private String postCode;        //邮政编码
    private String detailAddress;   //详细地址(街道)
    private String defaultStatus;   //是否为默认
}
