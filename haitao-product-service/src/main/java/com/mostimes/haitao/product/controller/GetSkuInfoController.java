package com.mostimes.haitao.product.controller;

import com.mostimes.haitao.entity.PmsSkuInfo;
import com.mostimes.haitao.product.service.ProductSkuService;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GetSkuInfoController {
    @Autowired
    ProductSkuService productSkuService;

    @RequestMapping(value = "/getSkuInfo/{saleAttrIdList}")
    @HystrixCommand(fallbackMethod = "hystrix")
    public PmsSkuInfo getSkuInfo(@PathVariable("saleAttrIdList") String saleAttrIdList) {
        return productSkuService.getProductSku(saleAttrIdList);
    }

    public PmsSkuInfo hystrix(@PathVariable("saleAttrIdList") String saleAttrIdList) { return null; }
}
