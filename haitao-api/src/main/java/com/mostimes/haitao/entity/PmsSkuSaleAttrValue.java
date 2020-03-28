package com.mostimes.haitao.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class PmsSkuSaleAttrValue implements Serializable {
    private String id;                  //id
    private int skuId;               //库存单元id
    private String saleAttrId;          //销售属性id
    private int saleAttrValueId;        //销售属性值id
    private String saleAttrName;        //销售属性名称
    private String saleAttrValueName;   //销售属性值名称
}
