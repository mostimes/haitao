package com.mostimes.haitao.entity;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class PmsProduct implements Serializable {
        private String id;                      //id
        private String productName;             //标题
        private String productAssistantName;    //副标题
        private int productDiscount;         //折扣
        private String productExplain;          //说明
        private BigDecimal productFreight;          //邮费
        private BigDecimal productPrice;            //商品价格
        private String productCategoryId;       //三级类目id
        private String productMainPic;          //商品主图
        private String productAllMainPic;       //全部主图url，用逗号隔开
        private BigDecimal productStar;             //星数
        private int productStatus;              //状态： 0->在售；1->下架；2->删除
        private String productType;             //首页商品 类型 0->特色产品；1->新品上架；2->精选；3->流行；4->特价中；5->普通产品
        private Date createTime;                //创建时间
        private Date undercarriageTime;         //下架时间
        private Date lastModifyTime;            //最后修改时间
}


