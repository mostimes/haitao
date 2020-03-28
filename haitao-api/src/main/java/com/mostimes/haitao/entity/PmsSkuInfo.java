package com.mostimes.haitao.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class PmsSkuInfo implements Serializable {
    private int id;              //库存id(itemID)
    private String productId;      //商品id
    private double price;           //价格
    private String skuName;         //sku名称
    private int stock;              //库存
    private String catalogId;       //三级分类id
    private String skuDefaultImg;   //默认显示图片
}
