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
public class DeleteShipAddressController {
    @Autowired
    UserService userService;

    @RequestMapping(value = "/deleteShipAddress/{userId}/{addressId}")
    @HystrixCommand(fallbackMethod = "hystrix")
    public List<UmsMemberReceiveAddress> deleteShipAddress(@PathVariable("userId") String userId, @PathVariable("addressId") String addressId){
        return userService.deleteShipAddress(userId,addressId);
    }

    public List<UmsMemberReceiveAddress> hystrix(@PathVariable("userId") String userId, @PathVariable("addressId") String addressId){
        return null;
    }
}
