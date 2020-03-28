package com.mostimes.haitao.product.controller;

import com.mostimes.haitao.entity.CmsCarousel;
import com.mostimes.haitao.product.service.CarouselService;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class GetCarouselController {
    @Autowired
    CarouselService carouselService;

    @RequestMapping(value = "getCarousel/{type}")
    @HystrixCommand(fallbackMethod = "hystrix")
    public List<CmsCarousel> getCarousel(@PathVariable("type") String type){
        return carouselService.getCarousel(type);
    }

    public List<CmsCarousel> hystrix(@PathVariable("type") String type){
        return null;
    }
}
