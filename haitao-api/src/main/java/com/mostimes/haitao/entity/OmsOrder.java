package com.mostimes.haitao.entity;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class OmsOrder {
    private String  id;                     //订单id
    private String  memberId;               //会员id
    private int  productQuantity;           //购买商品数量
    private Date createTime;                //提交时间
    private BigDecimal totalAmount;         //订单总金额
    private BigDecimal freightAmount;      //运费金额
    private BigDecimal couponAmount;       //优惠券抵扣金额
    private BigDecimal payAmount;          //应付金额（实际支付金额）
    private int payType;                   //支付方式：0->支付宝；1->货到付款；2->银行卡
    private int sourceType;                //订单来源：0->PC订单；1->app订单
    private int status;                    //订单状态：0->待发货；1->待确认收货；2->待评价；3->已完成
    private String  deliveryCompany;        //物流公司(配送方式)
    private String  deliverySn;             //物流单号
    private int autoConfirmDay;            //自动确认时间（天）
    private String  receiveName;            //收货人姓名
    private String  receivePhone;           //收货人电话
    private String  receivePostcode;        //收货人邮编
    private String  receiveAddress;         //收货人详细地址
    private String  note;                   //订单备注
    private int deleteStatus;              //删除状态：0->未删除；1->已删除
    private Date  deliveryTime;             //发货时间
    private Date  receiveTime;              //确认收货时间
    private Date  commentTime;              //评价时间
}
