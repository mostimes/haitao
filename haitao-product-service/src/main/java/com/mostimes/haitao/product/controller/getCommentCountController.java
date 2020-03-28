package com.mostimes.haitao.product.controller;

import com.mostimes.haitao.entity.PmsCommentCount;
import com.mostimes.haitao.product.service.ProductService;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class getCommentCountController {
    @Autowired
    ProductService productService;

    @RequestMapping(value = "/getCommentCountById/{productId}")
    @HystrixCommand(fallbackMethod = "hystrix")
    public PmsCommentCount getCommentCount(@PathVariable("productId") String productId){
        return productService.getCommentCountByProductId(productId);
    }

    public PmsCommentCount hystrix(@PathVariable("productId") String productId){
        return null;
    }
}
