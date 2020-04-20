package com.mostimes.haitao.entity;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class OmsCart {
    private String id;                      //id
    private String memberId;               //会员id
    private String productId;              //商品id
    private int quantity;                   //购买数量
    private BigDecimal productPrice;           //添加到购物车的价格
    private String productPic;             //商品主图
    private String productName;            //商品名称
    private String productSku;             //spu表id
    private BigDecimal productFreight;         //商品运费
    private String productCategoryId;     //商品三分类id
    private Date createDate;               //创建时间
    private Date modifyDate;               //最后修改时间
    private String productAttr;             //商品属性
    private int selectStatus;               //是否选择
}
