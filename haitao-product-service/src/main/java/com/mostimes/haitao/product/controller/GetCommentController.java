package com.mostimes.haitao.product.controller;

import com.mostimes.haitao.entity.PmsComment;
import com.mostimes.haitao.product.service.ProductService;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class GetCommentController {
    @Autowired
    ProductService productService;

    @RequestMapping(value = "/getCommentById/{productId}/{commentPage}/{type}")
    @HystrixCommand(fallbackMethod = "hystrix")
    public List<PmsComment> getComment(@PathVariable("productId") String productId, @PathVariable("commentPage") String page, @PathVariable("type") String type){
        return productService.getCommentByProductId(productId, page,type);
    }

    public List<PmsComment> hystrix(@PathVariable("productId") String productId, @PathVariable("commentPage") String page, @PathVariable("type") String type) {
        return null;
    }
}
