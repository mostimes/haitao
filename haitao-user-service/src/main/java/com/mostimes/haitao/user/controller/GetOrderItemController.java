package com.mostimes.haitao.user.controller;

import com.mostimes.haitao.entity.OmsOrderItem;
import com.mostimes.haitao.user.service.UserOrderService;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class GetOrderItemController {
    @Autowired
    UserOrderService userOrderService;

    @RequestMapping(value = "/getOrderItem/{orderIdList}")
    @HystrixCommand(fallbackMethod = "hystrix")
    public List<List<OmsOrderItem>> getOrderItem(@PathVariable("orderIdList") String orderIdList){
        return userOrderService.getOrderItem(orderIdList);
    }

    public List<List<OmsOrderItem>> hystrix(@PathVariable("orderIdList") String orderIdList){
        return null;
    }
}
