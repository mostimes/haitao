package com.mostimes.haitao.user.controller;

import com.mostimes.haitao.entity.UmsMember;
import com.mostimes.haitao.user.service.UserService;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginController {
    @Autowired
    UserService userService;

    @RequestMapping(value = "/login/{userName}/{password}/{type}/{ip}")
    @HystrixCommand(fallbackMethod = "hystrix")
    public UmsMember login(@PathVariable("userName") String userName, @PathVariable("password") String password, @PathVariable("type") String type,@PathVariable("ip") String ip){
        return userService.login(userName,password,type,ip);
    }

    public UmsMember hystrix(@PathVariable("userName") String userName, @PathVariable("password") String password, @PathVariable("type") String type,@PathVariable("ip") String ip) {
        return null;
    }
}
