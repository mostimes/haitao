package com.mostimes.haitao.user.controller;

import com.mostimes.haitao.user.service.UserCartService;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AddCartController {
    @Autowired
    UserCartService userCartService;

    @RequestMapping(value = "/addCart/{userId}/{productId}/{quantity}/{productPrice}/{skuId}")
    @HystrixCommand(fallbackMethod = "hystrix")
    public Boolean addCart(@PathVariable("userId") String userId, @PathVariable("productId") String productId, @PathVariable("quantity") String quantity, @PathVariable("productPrice") String productPrice, @PathVariable("skuId") String skuId){
        return userCartService.addCart(userId,productId,quantity,productPrice,skuId);
    }

    public Boolean hystrix(@PathVariable("userId") String userId, @PathVariable("productId") String productId, @PathVariable("quantity") String quantity, @PathVariable("productPrice") String productPrice, @PathVariable("skuId") String skuId){
        return false;
    }
}
