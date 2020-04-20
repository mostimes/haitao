package com.mostimes.haitao.user.controller;

import com.mostimes.haitao.entity.OmsOrder;
import com.mostimes.haitao.user.service.UserOrderService;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GetOrderByIdController {
    @Autowired
    UserOrderService userOrderService;

    @RequestMapping(value = "/getOrderById/{orderId}")
    @HystrixCommand(fallbackMethod = "hystrix")
    public OmsOrder getOrderById(@PathVariable("orderId") String orderId){
        return userOrderService.getOrderById(orderId);
    }

    public OmsOrder hystrix(@PathVariable("orderId") String orderId){
        return null;
    }
}
