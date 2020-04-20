package com.mostimes.haitao.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class PmsProductCount implements Serializable {
    private String id;              //id
    private String productId;       //商品id
    private int collectCount;       //收藏总量
    private int visitCount;         //访问总量
    private int saleCount;          //销售总量
    private int favourableComment;  //好评
    private int mediumComment;      //中评
    private int badComment;         //差评
}
