package com.mostimes.haitao.entity;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class PmsComment implements Serializable {
    private int id;                     //id
    private String productId;           //商品id
    private String memberNickName;      //用户昵称
    private BigDecimal star;                //评价星数：0->5
    private String appraise;            //评价等级：0->好评；1->中评；2->差评
    private Date createTime;            //评价时间
    private String productBusinReplay;  //商家回复
    private int showStatus;             //是否显示
    private String productColor;        //购买时的商品颜色
    private String productSize;         //购买时的商品尺寸
    private String content;             //评价内容
    private String pics;                //上传图片地址，以逗号隔开
    private String memberIcon;          //评论用户头像
}
