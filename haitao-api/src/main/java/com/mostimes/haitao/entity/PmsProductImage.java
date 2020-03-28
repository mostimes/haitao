package com.mostimes.haitao.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class PmsProductImage implements Serializable {
    private String id;          //id
    private String productId;   //商品id
    private String imgUrl;      //图片地址
}
