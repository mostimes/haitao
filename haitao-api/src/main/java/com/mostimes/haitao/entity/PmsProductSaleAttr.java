package com.mostimes.haitao.entity;

import lombok.Data;

@Data
public class PmsProductSaleAttr {
    private int id;              //id
    private String productId;       //商品id
    private String saleAttrId;      //销售属性id
    private String saleAttrName;    //销售属性名称
}
