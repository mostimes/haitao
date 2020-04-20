package com.mostimes.haitao.service;

import com.mostimes.haitao.entity.*;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Map;


@FeignClient(value = "PROVIDER-PRODUCT")
public interface ProductService {
    @RequestMapping(value = "/getProductById/{productId}")
    PmsProduct getProductById(@PathVariable("productId") String productId);

    @RequestMapping(value = "/getProductImages/{product_id}")
    PmsProductImage getProductImage(@PathVariable("product_id") String productId);

    @RequestMapping(value = "/getRecommendProduct/{sort}")
    List<PmsProduct> getRecommendProduct(@PathVariable("sort") String sort);

    @RequestMapping(value = "/getCommentById/{productId}/{commentPage}/{type}")
    List<PmsComment> getComment(@PathVariable("productId") String productId, @PathVariable("commentPage") String page, @PathVariable("type") String type);

    @RequestMapping(value = "/getProductCountById/{productId}")
    PmsProductCount getProductCount(@PathVariable("productId") String productId);

    @RequestMapping(value = "/gerProductAttr/{productId}")
    Map<String, List<PmsProductSaleAttrValue>> getProductAttr(@PathVariable("productId") String productId);

    @RequestMapping(value = "/getSkuInfo/{saleAttrIdList}")
    PmsSkuInfo getSkuInfo(@PathVariable("saleAttrIdList") String saleAttrIdList);

    @RequestMapping(value = "getCarousel/{type}")
    List<CmsCarousel> getCarousel(@PathVariable("type") String type);

    @RequestMapping(value = "/fluctuateProductCount/{productId}/{type}/{fluctuate}")
    void fluctuateProductCount(@PathVariable("productId") String productId, @PathVariable("type") String type, @PathVariable("fluctuate") String fluctuate);
}
