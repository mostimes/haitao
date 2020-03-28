package com.mostimes.haitao.product.service.serviceImpl;


import com.alibaba.fastjson.JSON;
import com.mostimes.haitao.entity.PmsSkuInfo;
import com.mostimes.haitao.entity.PmsSkuSaleAttrValue;
import com.mostimes.haitao.product.mapper.ProductSkuInfoMapper;
import com.mostimes.haitao.product.mapper.ProductSkuSaleAttrValueMapper;
import com.mostimes.haitao.product.service.ProductSkuService;
import com.mostimes.haitao.util.RedisUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;

import java.util.*;

@Service
public class ProductSkuServiceImpl implements ProductSkuService {
    @Autowired
    ProductSkuInfoMapper productSkuInfoMapper;
    @Autowired
    ProductSkuSaleAttrValueMapper productSkuSaleAttrValueMapper;
    @Autowired
    RedisUtil redisUtil;

    @Override
    //获取商品库存信息
    public PmsSkuInfo getProductSku(String saleAttrIdList) {
        Jedis jedis = null;
        List<String> saleAttrIds = Arrays.asList(saleAttrIdList.split(","));
        PmsSkuInfo pmsSkuInfo;
        int skuId = getProductSkuIdFromDB(saleAttrIds);
        try{
            jedis = redisUtil.getJedis();
            if (jedis != null){
                String skuIdStr = jedis.get("productSku:skuId:" + skuId +":info");
                if (StringUtils.isNotBlank(skuIdStr)){
                    pmsSkuInfo = JSON.parseObject(skuIdStr, PmsSkuInfo.class);
                }else {
                    pmsSkuInfo = getProductInfoFromDB(skuId, 0);
                }
            }else { //开启redis失败，尝试从数据库查找
                pmsSkuInfo = getProductInfoFromDB(skuId, 1);
            }
            return pmsSkuInfo;
        }finally {
            jedis.close();
        }
    }

    //从数据库中查找skuId
    public int getProductSkuIdFromDB(List<String> saleAttrIds){
        List<Integer> skuIdList = new ArrayList<Integer>();
        for (String attrValueId : saleAttrIds){
            List<PmsSkuSaleAttrValue> tempSkuSaleAttrValueList = new ArrayList<>();

            Map<String,Object> map = new HashMap<>();
            map.put("sale_attr_value_id", attrValueId);
            tempSkuSaleAttrValueList = productSkuSaleAttrValueMapper.selectByMap(map);

            for (PmsSkuSaleAttrValue skuSaleAttrValue : tempSkuSaleAttrValueList){
                skuIdList.add(skuSaleAttrValue.getSkuId());
            }
        }
        return getRightSkuId(skuIdList, saleAttrIds);
    }

    //找出正确的sku
    public int getRightSkuId(List<Integer> skuIdList, List<String> saleAttrIds){
        Collections.sort(skuIdList);
        int count = 1,rightSkuId = skuIdList.get(0);
        for (int i = 1; i < skuIdList.size(); i++){
            if (skuIdList.get(i) == rightSkuId){
                count++;
                if (count == saleAttrIds.size()){
                    break;
                }
            }else {
                rightSkuId = skuIdList.get(i);
                count = 1;
            }
        }
        return rightSkuId;
    }

    //从数据库中查找sku信息
    public PmsSkuInfo getProductInfoFromDB(int skuId, int cache){
        PmsSkuInfo skuInfo;
        skuInfo = productSkuInfoMapper.selectById(skuId);
        if (cache == 0){
            Jedis jedis = null;
            try{
                jedis = redisUtil.getJedis();
                if (jedis != null){
                    jedis.set("productSku:skuId:" + skuId +":info", JSON.toJSONString(skuInfo));
                }
            }finally {
                jedis.close();
            }
        }
        return skuInfo;
    }
}
