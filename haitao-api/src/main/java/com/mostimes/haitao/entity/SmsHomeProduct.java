package com.mostimes.haitao.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class SmsHomeProduct implements Serializable {
    private String id;              //id
    private String productId;       //商品id
    private String productName;     //商品名称
    private double productStar;    //商品星数：0->5
    private String productMainPic;  //第一张主图
    private double productPrice;    //商品价格
    private String sort;            //首页商品类型 0->特色产品；1->新品上架；2->精选；3->流行；4->特价中
}
