package com.mostimes.haitao.user.controller;

import com.mostimes.haitao.user.service.UserCartService;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DeleteCartProductController {
    @Autowired
    UserCartService userCartService;

    @RequestMapping(value = "/deleteCartProduct/{userId}/{cartId}")
    @HystrixCommand(fallbackMethod = "hystrix")
    public Boolean deleteCartProduct(@PathVariable("userId") String userId,@PathVariable("cartId") String cartId){
        return userCartService.deleteCartProduct(userId,cartId);
    }

    public Boolean hystrix(@PathVariable("userId") String userId,@PathVariable("cartId") String cartId){
        return false;
    }
}
