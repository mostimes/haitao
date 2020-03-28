package com.mostimes.haitao.product.controller;

import com.mostimes.haitao.entity.PmsProductSaleAttrValue;
import com.mostimes.haitao.product.service.ProductAttrService;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
public class GetProductAttrController {
    @Autowired
    ProductAttrService productAttrService;

    @RequestMapping(value = "/gerProductAttr/{productId}")
    @HystrixCommand(fallbackMethod = "hystrix")
    public Map<String, List<PmsProductSaleAttrValue>> getProductAttr(@PathVariable("productId") String productId){
        return productAttrService.getProductAttrByProductId(productId);
    }

    public Map<String, List<PmsProductSaleAttrValue>> hystrix(@PathVariable("productId") String productId) {
        return null;
    }
}
