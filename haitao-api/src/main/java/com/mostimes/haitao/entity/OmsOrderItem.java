package com.mostimes.haitao.entity;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class OmsOrderItem {
    private String id;                  //id
    private String orderId;             //订单id
    private String productId;           //商品id
    private String productPic;          //主图
    private String productName;         //标题
    private BigDecimal productPrice;    //价格
    private int productQuantity;        //数量
    private String productSkuId;        //skuId
    private String productCategoryId;   //分类id
    private String productAttr;         //商品销售属性:[{"key":"颜色","value":"颜色"},{"key":"容量","value":"4G"}]
}
