package com.mostimes.haitao.user.controller;

import com.mostimes.haitao.entity.UmsMemberReceiveAddress;
import com.mostimes.haitao.user.service.UserService;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class GetShipAddressController {
    @Autowired
    UserService userService;

    @RequestMapping(value = "/getShipAddress/{userId}")
    @HystrixCommand(fallbackMethod = "hystrix")
    public List<UmsMemberReceiveAddress> getShipAddress(@PathVariable("userId") String userId){
        return userService.getShipAddress(userId);
    }

    public List<UmsMemberReceiveAddress> hystrix(String userId){
        return null;
    }
}
