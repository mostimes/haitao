package com.mostimes.haitao.user.controller;

import com.mostimes.haitao.user.service.UserOrderService;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SubmitOrderController {
    @Autowired
    UserOrderService userOrderService;

    @RequestMapping(value = "/submitOrder/{userId}/{cartIds}/{addressId}/{patType}")
    @HystrixCommand(fallbackMethod = "hystrix")
    public int submitOrder(@PathVariable("userId") String userId, @PathVariable("cartIds") String cartIds, @PathVariable("addressId") String addressId, @PathVariable("patType") String patType){
        return userOrderService.submitOrder(userId,cartIds,addressId,patType);
    }

    public int hystrix(@PathVariable("userId") String userId, @PathVariable("cartIds") String cartIds, @PathVariable("addressId") String addressId, @PathVariable("patType") String patType){
        return -1;
    }
}
