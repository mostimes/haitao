package com.mostimes.haitao.user.controller;

import com.mostimes.haitao.user.service.UserCartService;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
public class GetCartProductAttrController {
    @Autowired
    UserCartService userCartService;

    @RequestMapping(value = "/getCartProductAttr/{skuIdList}")
    @HystrixCommand(fallbackMethod = "hystrix")
    public List<Map<String, String>> getCartProductAttr(@PathVariable("skuIdList") String skuIdList){
        return userCartService.getCartProductAttr(skuIdList);
    }

    public List<Map<String, String>> hystrix(@PathVariable("skuIdList") String skuIdList){
        return null;
    }
}
