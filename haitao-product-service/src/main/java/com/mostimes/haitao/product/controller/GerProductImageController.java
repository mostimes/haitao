package com.mostimes.haitao.product.controller;

import com.mostimes.haitao.entity.PmsProductImage;
import com.mostimes.haitao.product.service.ProductService;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GerProductImageController {
    @Autowired
    ProductService productService;

    @RequestMapping(value = "/getProductImages/{product_id}")
    @HystrixCommand(fallbackMethod = "hystrix")
    public PmsProductImage getProductImage(@PathVariable("product_id") String productId){
        return productService.getProductImage(productId);
    }

    public PmsProductImage hystrix(@PathVariable("product_id") String productId){
        return null;
    }
}
