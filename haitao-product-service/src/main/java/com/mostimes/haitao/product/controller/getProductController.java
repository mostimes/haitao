package com.mostimes.haitao.product.controller;

import com.mostimes.haitao.entity.PmsProduct;
import com.mostimes.haitao.product.service.ProductService;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GetProductController {
    @Autowired
    ProductService productService;

    @RequestMapping(value = "/getProductById/{productId}")
    @HystrixCommand(fallbackMethod = "hystrix")
    public PmsProduct getProductById(@PathVariable("productId") String productId){
        return productService.getProductById(productId);
    }

    public PmsProduct hystrix(@PathVariable("productId") String productId) {
        PmsProduct pmsProduct = new PmsProduct();
        pmsProduct.setId("0");
        return pmsProduct;
    }
}
