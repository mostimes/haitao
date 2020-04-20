package com.mostimes.haitao.user.controller;

import com.mostimes.haitao.entity.UmsMember;
import com.mostimes.haitao.user.service.UserService;
import com.mostimes.haitao.util.MD5Util;
import com.mostimes.haitao.util.UUIDUtil;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.Map;

@RestController
public class RegisterController {
    @Autowired
    UserService userService;

    @RequestMapping(value = "/register/{nickName}/{tel}/{eMail}/{passWord}/{payPassWord}")
    @HystrixCommand(fallbackMethod = "hystrix")
    public Map<String, Object> register(@PathVariable("nickName") String nickName,@PathVariable("tel") String tel,@PathVariable("eMail") String eMail,@PathVariable("passWord") String passWord,@PathVariable("payPassWord") String payPassWord) {
        UmsMember umsMember = new UmsMember();
        umsMember.setId(UUIDUtil.createId());
        umsMember.setStatus("1");
        umsMember.setCreateTime(new Date());
        umsMember.setEmail(eMail);
        umsMember.setGender("0");
        umsMember.setNickname(nickName);
        umsMember.setPassword(MD5Util.md5(passWord));
        umsMember.setPayPassword(MD5Util.md5(payPassWord));
        umsMember.setPhone(tel);
        return userService.register(umsMember);
    }

    public Map<String, Object> hystrix(@PathVariable("nickName") String nickName,@PathVariable("tel") String tel,@PathVariable("eMail") String eMail,@PathVariable("passWord") String passWord,@PathVariable("payPassWord") String payPassWord) {
        return null;
    }
}
