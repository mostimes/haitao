package com.mostimes.haitao.user.controller;

import com.mostimes.haitao.entity.OmsCart;
import com.mostimes.haitao.user.service.UserCartService;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class GetCartProductController {
    @Autowired
    UserCartService userCartService;

    @RequestMapping(value = "/getCartProduct/{userId}")
    @HystrixCommand(fallbackMethod = "hystrix")
    public List<OmsCart> getCartProduct(@PathVariable("userId") String userId){
        return userCartService.getCartProduct(userId);
    }

    public List<OmsCart> hystrix(@PathVariable("userId") String userId){
        return null;
    }
}
