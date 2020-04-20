package com.mostimes.haitao.product.controller;

import com.mostimes.haitao.entity.PmsProduct;
import com.mostimes.haitao.entity.SmsHomeProduct;
import com.mostimes.haitao.product.service.HomeProductService;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class GetRecommendProductController {
    @Autowired
    HomeProductService homeProductService;

    @RequestMapping(value = "/getRecommendProduct/{sort}")
    @HystrixCommand(fallbackMethod = "hystrix")
    public List<PmsProduct> getRecommendProduct(@PathVariable("sort") String sort) {
        return homeProductService.getRecommendProduct(sort);
    }

    public List<PmsProduct> hystrix(@PathVariable("sort") String sort) { return null; }
}
