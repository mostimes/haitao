package com.mostimes.haitao.product.controller;

import com.mostimes.haitao.entity.*;
import com.mostimes.haitao.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin
public class ProductController {
    @Autowired
    ProductService productService;

    @RequestMapping(value = "/product/getProductById")
    public PmsProduct getProductById(String productId){
        return productService.getProductById(productId);
    }

    @RequestMapping(value = "/product/getProductImage")
    public PmsProductImage getProductImage(String productId){ return productService.getProductImage(productId); }

    @RequestMapping(value = "/product/getHomeProduct")
    public List<PmsProduct> getRecommendProduct(String sort){
        return productService.getRecommendProduct(sort);
    }

    @RequestMapping(value = "/product/getCommentByProductId")
    public List<PmsComment> getCommentByProductId(String productId, String page, String type){ return productService.getComment(productId, page, type); }

    @RequestMapping(value = "/product/getProductCountByProductId")
    public PmsProductCount getProductCountByProductId(String productId){ return productService.getProductCount(productId); }

    @RequestMapping(value = "/product/getProductAttr")
    public Map<String, List<PmsProductSaleAttrValue>> getProductAttr(String productId){ return productService.getProductAttr(productId); }

    @RequestMapping(value = "/product/getSkuInfo")
    public PmsSkuInfo getSkuInfo(String saleAttrIdList){
        return productService.getSkuInfo(saleAttrIdList);
    }

    @RequestMapping(value = "/product/getCarousel")
    public List<CmsCarousel> getCarousel(String type){
        return productService.getCarousel(type);
    }

    @RequestMapping(value = "/product/fluctuateProductCount")
    public void fluctuateProductCount(String productId, String type, String fluctuate){
        productService.fluctuateProductCount(productId, type, fluctuate);
    }
}
