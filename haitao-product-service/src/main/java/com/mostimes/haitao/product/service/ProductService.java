package com.mostimes.haitao.product.service;

import com.mostimes.haitao.entity.PmsComment;
import com.mostimes.haitao.entity.PmsProductCount;
import com.mostimes.haitao.entity.PmsProduct;
import com.mostimes.haitao.entity.PmsProductImage;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public interface ProductService {
    PmsProduct getProductById(String productId); //用id获取商品

    PmsProductImage getProductImage(String productId); //获取商品描述图片

    PmsProductCount getProductCountByProductId(String productId); //用商品id查询评价统计

    List<PmsComment> getCommentByProductId(String productId, String page,String type); //用商品id获取评论

    void fluctuateProductCount(String productId, String type, int fluctuate);
}
