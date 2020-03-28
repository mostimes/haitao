package com.mostimes.haitao.product.service;

import com.mostimes.haitao.entity.PmsProductSaleAttrValue;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public interface ProductAttrService {
    Map<String, List<PmsProductSaleAttrValue>> getProductAttrByProductId(String productId); //用商品id查询商品属性
}
