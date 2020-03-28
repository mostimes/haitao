package com.mostimes.haitao.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class PmsProductSaleAttrValue implements Serializable {
    private int id;                  //id
    private String productId;           //商品id
    private String saleAttrId;          //销售属性id
    private String saleAttrValueName;   //销售属性值名称
}
