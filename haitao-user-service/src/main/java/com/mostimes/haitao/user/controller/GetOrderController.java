package com.mostimes.haitao.user.controller;

import com.mostimes.haitao.entity.OmsOrder;
import com.mostimes.haitao.user.service.UserOrderService;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class GetOrderController {
    @Autowired
    UserOrderService userOrderService;

    @RequestMapping(value = "/getOrder/{userId}/{orderPage}")
    @HystrixCommand(fallbackMethod = "hystrix")
    public List<OmsOrder> getOrder(@PathVariable("userId") String userId, @PathVariable("orderPage") String orderPage){
        return userOrderService.getOrder(userId,orderPage);
    }

    public List<OmsOrder> hystrix(@PathVariable("userId") String userId, @PathVariable("orderPage") String orderPage){
        return null;
    }
}
