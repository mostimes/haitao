package com.mostimes.haitao.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class PmsProduct implements Serializable {
        private String id;                      //id
        private String productName;             //标题
        private String productAssistantName;    //副标题
        private double productDiscount;         //折扣
        private double productPrice;            //价格
        private String productExplain;          //说明
        private double productFreight;          //邮费
        private String productCategoryId;       //三级类目id
        private String productPicId;            //描述图片表id
        private String productMainPic;          //主图url
        private String productAllMainPic;       //全部主图url，用逗号隔开
        private double productStar;             //星数
        private int productSaleNum;             //销售总量
        private int productVisitNum;            //查看总量
        private int productCommentNum;          //评价总数
        private int productCollectNum;          //收藏总数
        private int productStatus;              //状态： 0->在售；1->下架；2->删除
        private String productType;             //首页商品 类型 0->特色产品；1->新品上架；2->精选；3->流行；4->特价中；5->普通产品
        private Date createTime;                //创建时间
        private Date undercarriageTime;         //下架时间
        private Date lastModifyTime;            //最后修改时间
}


