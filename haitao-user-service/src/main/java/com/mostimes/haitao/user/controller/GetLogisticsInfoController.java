package com.mostimes.haitao.user.controller;

import com.mostimes.haitao.user.service.UserOrderService;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GetLogisticsInfoController {
    @Autowired
    UserOrderService userOrderService;

    @RequestMapping(value = "/getLogisticsInfo/{code}")
    @HystrixCommand(fallbackMethod = "hystrix")
    public String getLogisticsInfo(@PathVariable("code") String code){
        return userOrderService.getLogisticsInfo(code);
    }

    public String hystrix(@PathVariable("code") String code){
        return null;
    }
}
