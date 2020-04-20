package com.mostimes.haitao.user.controller;

import com.mostimes.haitao.user.service.UserOrderService;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GetOrderCountController {
    @Autowired
    UserOrderService userOrderService;

    @RequestMapping(value = "/getOrderCount/{userId}")
    @HystrixCommand(fallbackMethod = "hystrix")
    public int getOrderCount(@PathVariable("userId") String userId){
        return userOrderService.getOrderCount(userId);
    }

    public int hystrix(@PathVariable("userId") String userId){
        return 0;
    }
}
