package com.mostimes.haitao.user.controller;

import com.mostimes.haitao.entity.UmsMember;
import com.mostimes.haitao.user.service.UserService;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GetPersonalInfoController {
    @Autowired
    UserService userService;

    @RequestMapping(value = "/getPersonalInfo/{userId}")
    @HystrixCommand(fallbackMethod = "hystrix")
    public UmsMember getPersonalInfo(@PathVariable("userId") String userId){
        return userService.getPersonalInfo(userId);
    }

    public UmsMember hystrix(@PathVariable("userId") String userId){
        return null;
    }
}
