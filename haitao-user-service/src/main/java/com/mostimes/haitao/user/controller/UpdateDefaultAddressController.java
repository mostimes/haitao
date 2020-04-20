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
public class UpdateDefaultAddressController {
    @Autowired
    UserService userService;

    @RequestMapping(value = "/updateDefaultAddress/{userId}/{oldAddressId}/{newAddressId}")
    @HystrixCommand(fallbackMethod = "hystrix")
    public List<UmsMemberReceiveAddress> updateDefaultAddress(@PathVariable("userId") String userId,@PathVariable("oldAddressId") String oldAddressId,@PathVariable("newAddressId") String newAddressId){
        return userService.updateDefaultAddress(userId,oldAddressId,newAddressId);
    }

    public List<UmsMemberReceiveAddress> hystrix(@PathVariable("userId") String userId,@PathVariable("oldAddressId") String oldAddressId,@PathVariable("newAddressId") String newAddressId){
        return null;
    }
}
