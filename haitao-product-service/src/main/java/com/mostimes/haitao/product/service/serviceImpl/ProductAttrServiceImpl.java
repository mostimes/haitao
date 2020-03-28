package com.mostimes.haitao.product.service.serviceImpl;

import com.alibaba.fastjson.JSON;
import com.mostimes.haitao.entity.PmsComment;
import com.mostimes.haitao.entity.PmsProductSaleAttr;
import com.mostimes.haitao.entity.PmsProductSaleAttrValue;
import com.mostimes.haitao.product.mapper.ProductSaleAttrMapper;
import com.mostimes.haitao.product.mapper.ProductSaleAttrValueMapper;
import com.mostimes.haitao.product.service.ProductAttrService;
import com.mostimes.haitao.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ProductAttrServiceImpl implements ProductAttrService {
    @Autowired
    ProductSaleAttrValueMapper productSaleAttrValueMapper;
    @Autowired
    ProductSaleAttrMapper productSaleAttrMapper;
    @Autowired
    RedisUtil redisUtil;

    @Override
    //用商品id查询商品属性
    public Map<String, List<PmsProductSaleAttrValue>> getProductAttrByProductId(String productId) {
        Jedis jedis = null;

        List<PmsProductSaleAttr> saleAttrList = new ArrayList<>();
        Map<String, List<PmsProductSaleAttrValue>> saleAttrValueMap = new HashMap<>();
        try{
            jedis = redisUtil.getJedis();
            if (jedis != null){
                List<String> saleAttrListStr = jedis.lrange( "productAttr:productId:" + productId +":info", -1, 0);
                if (!saleAttrListStr.isEmpty()){ // 该商品属性已存在redis
                    for (String str : saleAttrListStr){
                        saleAttrList.add(JSON.parseObject(str, PmsProductSaleAttr.class));
                    }
                    return getProductAttrValueByAttrId(saleAttrList);
                }else { //用户未在redis中，查询数据库
                    saleAttrList = getProductSaleAttrFromDB(productId,0);
                    for (PmsProductSaleAttr saleAttr : saleAttrList){
                        saleAttrValueMap.put(saleAttr.getSaleAttrName(),getProductSaleAttrValueFromDB(saleAttr, 0));
                    }
                }
            }else { //开启redis失败，尝试从数据库查找
                saleAttrList = getProductSaleAttrFromDB(productId,1);
                for (PmsProductSaleAttr saleAttr : saleAttrList){
                    saleAttrValueMap.put(saleAttr.getSaleAttrName(),getProductSaleAttrValueFromDB(saleAttr, 1));
                }
            }
            return saleAttrValueMap;
        }finally {
            jedis.close();
        }
    }

    //用商品id从数据库中获取商品属性
    public List<PmsProductSaleAttr> getProductSaleAttrFromDB(String productId, int cache){
        Jedis jedis = null;
        List<PmsProductSaleAttr> saleAttrList = new ArrayList<>();
        Map<String,Object> map = new HashMap<>();
        map.put("product_id", productId);
        saleAttrList = productSaleAttrMapper.selectByMap(map);
        if (cache == 0){
            try {
                jedis = redisUtil.getJedis();
                if (jedis != null){
                    jedis.del("productAttr:productId:" + productId +":info");
                    for (PmsProductSaleAttr saleAttr : saleAttrList){
                        jedis.lpush("productAttr:productId:" + productId +":info", JSON.toJSONString(saleAttr));
                    }
                }
            }finally {
                jedis.close();
            }
        }
        return saleAttrList;
    }


    //用属性id查询属性值
    public Map<String, List<PmsProductSaleAttrValue>> getProductAttrValueByAttrId(List<PmsProductSaleAttr> saleAttrs) {
        Jedis jedis = null;

        Map<String, List<PmsProductSaleAttrValue>> saleAttrValueMap = new HashMap<>();
        try {
            jedis = redisUtil.getJedis();
            if (jedis != null) {
                for (PmsProductSaleAttr saleAttr : saleAttrs) {
                    List<PmsProductSaleAttrValue> saleAttrValueList = new ArrayList<>();
                    List<String> saleAttrListStr = jedis.lrange("productAttrValue:" + saleAttr.getProductId() + ":" + saleAttr.getSaleAttrId() + ":info", -1, 0);
                    if (!saleAttrListStr.isEmpty()) { //该数据已存在redis
                        for (String saleAttrValue : saleAttrListStr) {
                            saleAttrValueList.add(JSON.parseObject(saleAttrValue, PmsProductSaleAttrValue.class));
                        }
                        saleAttrValueMap.put(saleAttr.getSaleAttrName(), saleAttrValueList);
                    } else { //该数据未存在redis，从数据库中查询
                        saleAttrValueMap.put(saleAttr.getSaleAttrName(),getProductSaleAttrValueFromDB(saleAttr, 0));
                    }
                }
            }else { //开启redis失败，查询数据库
                for (PmsProductSaleAttr saleAttr : saleAttrs) {
                    saleAttrValueMap.put(saleAttr.getSaleAttrName(),getProductSaleAttrValueFromDB(saleAttr, 1));
                }
            }
        }finally{
            jedis.close();
        }
        return saleAttrValueMap;
    }

    //用属性id从数据库中获取商品属性值
    public List<PmsProductSaleAttrValue> getProductSaleAttrValueFromDB(PmsProductSaleAttr saleAttr,int cache){
        Jedis jedis = null;

        List<PmsProductSaleAttrValue> saleAttrValueList = new ArrayList<>();
        Map<String,Object> map = new HashMap<>();
        map.put("product_id", saleAttr.getProductId());
        map.put("sale_attr_id", saleAttr.getSaleAttrId());
        saleAttrValueList =  productSaleAttrValueMapper.selectByMap(map);
        if (cache == 0){
            try {
                jedis = redisUtil.getJedis();
                if (jedis != null) {
                    jedis.del("productAttrValue:" + saleAttr.getProductId() + ":" + saleAttr.getSaleAttrId() + ":info");
                    for (PmsProductSaleAttrValue saleAttrValue : saleAttrValueList){
                        jedis.lpush("productAttrValue:" + saleAttr.getProductId() + ":" + saleAttr.getSaleAttrId() + ":info", JSON.toJSONString(saleAttrValue));
                    }
                }
            }finally {
                jedis.close();
            }
        }
        return saleAttrValueList;
    }
}
