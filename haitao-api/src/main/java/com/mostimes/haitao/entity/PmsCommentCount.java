package com.mostimes.haitao.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class PmsCommentCount implements Serializable {
    private String id;              //id
    private String productId;       //商品id
    private int allComment;         //全部评价
    private int favourableComment;  //好评
    private int mediumComment;      //中评
    private int badComment;         //差评
}
