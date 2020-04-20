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
public class EditAddressController {
    @Autowired
    UserService userService;

    @RequestMapping(value = "/user/editShipAddress/{addressId}/{userId}/{name}/{phone}/{postCode}/{detailAddress}/{defaultStatus}/{type}")
    @HystrixCommand(fallbackMethod = "hystrix")
    public List<UmsMemberReceiveAddress> editShipAddress(@PathVariable("addressId") String addressId,@PathVariable("userId") String userId,@PathVariable("name") String name,@PathVariable("phone") String phone,@PathVariable("postCode") String postCode,@PathVariable("detailAddress") String detailAddress,@PathVariable("defaultStatus") String defaultStatus,@PathVariable("type") String type){
        return userService.editShipAddress(addressId,userId,name,phone,postCode,detailAddress,defaultStatus,type);
    }

    public List<UmsMemberReceiveAddress> hystrix(@PathVariable("addressId") String addressId,@PathVariable("userId") String userId,@PathVariable("name") String name,@PathVariable("phone") String phone,@PathVariable("postCode") String postCode,@PathVariable("detailAddress") String detailAddress,@PathVariable("defaultStatus") String defaultStatus,@PathVariable("type") String type){
        return null;
    }
}
