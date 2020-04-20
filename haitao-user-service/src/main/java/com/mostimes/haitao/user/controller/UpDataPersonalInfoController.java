package com.mostimes.haitao.user.controller;

import com.mostimes.haitao.user.service.UserService;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UpDataPersonalInfoController {
    @Autowired
    UserService userService;

    @RequestMapping(value = "/upDataPersonalInfo/{userId}/{nickName}/{name}/{birthday}/{city}/{gender}/{job}")
    @HystrixCommand(fallbackMethod = "hystrix")
    public Boolean upDataPersonalInfo(@PathVariable("userId") String userId,@PathVariable("nickName") String nickName,@PathVariable("name") String name,@PathVariable("birthday") String birthday,@PathVariable("city") String city,@PathVariable("gender") String gender,@PathVariable("job") String job){
        return userService.upDataPersonalInfo(userId,nickName,name,birthday,city,gender,job);
    }

    public Boolean hystrix(@PathVariable("userId") String userId,@PathVariable("nickName") String nickName,@PathVariable("name") String name,@PathVariable("birthday") String birthday,@PathVariable("city") String city,@PathVariable("gender") String gender,@PathVariable("job") String job){
        return null;
    }
}
