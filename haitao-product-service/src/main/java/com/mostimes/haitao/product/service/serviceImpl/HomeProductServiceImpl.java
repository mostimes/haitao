package com.mostimes.haitao.product.service.serviceImpl;

import com.mostimes.haitao.entity.PmsProduct;
import com.mostimes.haitao.product.mapper.HomeProductMapper;
import com.mostimes.haitao.product.service.HomeProductService;
import com.mostimes.haitao.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class  HomeProductServiceImpl implements HomeProductService {
    @Autowired
    HomeProductMapper homeProductMapper;
    @Autowired
    RedisUtil redisUtil;

    @Override
    //获取首页商品
    public List<PmsProduct> getRecommendProduct(String productType) {
        Map<String, Object> map = new HashMap<>();
        map.put("product_type", productType);
        //获取首页商品
        List<PmsProduct> pmsProductList = homeProductMapper.selectByMap(map);
        return pmsProductList;
    }
}
