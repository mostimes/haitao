package com.mostimes.haitao.product.service.serviceImpl;

import com.alibaba.fastjson.JSON;
import com.mostimes.haitao.entity.CmsCarousel;
import com.mostimes.haitao.product.mapper.CarouselMapper;
import com.mostimes.haitao.product.service.CarouselService;
import com.mostimes.haitao.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CarouselServiceImpl implements CarouselService {
    @Autowired
    CarouselService carouselService;
    @Autowired
    CarouselMapper carouselMapper;
    @Autowired
    RedisUtil redisUtil;

    @Override
    //获取轮播信息
    public List<CmsCarousel> getCarousel(String type) {
        List<CmsCarousel> carouselList = new ArrayList<>();
        Jedis jedis = null;
        try {
            jedis = redisUtil.getJedis();
            if (jedis != null){
                List<String> carouselListStr = jedis.lrange("carousel:isEnabled:"+ type +":info",-1,0);
                if (!carouselListStr.isEmpty()){ //数据已存在redis
                    for (String carousel : carouselListStr){
                        carouselList.add(JSON.parseObject(carousel, CmsCarousel.class));
                    }
                }else { //数据未在redis，开启数据库
                    carouselList = getCarouselFromDB(type,0);
                }
            }else { //开启redis失败
                carouselList = getCarouselFromDB(type,1);
            }
        }finally {
            jedis.close();
        }
        return carouselList;
    }

    //从数据库中获取轮播信息
    public List<CmsCarousel> getCarouselFromDB(String type, int cache) {
        List<CmsCarousel> carouselList;
        Map<String, Object> map = new HashMap<>();
        map.put("is_enabled", type);
        carouselList = carouselMapper.selectByMap(map);
        if (cache == 0){
            Jedis jedis = null;
            try {
                jedis = redisUtil.getJedis();
                for (CmsCarousel carousel : carouselList){
                    jedis.lpush("carousel:isEnabled:"+ type +":info", JSON.toJSONString(carousel));
                }
            }finally {
                jedis.close();
            }
        }
        return carouselList;
    }
}
