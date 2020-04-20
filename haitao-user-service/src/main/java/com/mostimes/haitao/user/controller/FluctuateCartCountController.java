package com.mostimes.haitao.user.controller;

import com.mostimes.haitao.user.service.UserCartService;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FluctuateCartCountController {
    @Autowired
    UserCartService userCartService;

    @RequestMapping(value = "/fluctuateCartCount/{userId}/{cartId}/{count}")
    @HystrixCommand(fallbackMethod = "hystrix")
    public Boolean fluctuateCartCount(@PathVariable("userId") String userId,@PathVariable("cartId") String cartId,@PathVariable("count") String count){
        return userCartService.fluctuateCartCount(userId,cartId,count);
    }

    public Boolean hystrix(@PathVariable("userId") String userId,@PathVariable("cartId") String cartId,@PathVariable("count") String count){
        return false;
    }
}
