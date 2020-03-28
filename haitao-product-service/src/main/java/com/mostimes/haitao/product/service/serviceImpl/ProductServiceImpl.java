package com.mostimes.haitao.product.service.serviceImpl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mostimes.haitao.entity.PmsComment;
import com.mostimes.haitao.entity.PmsCommentCount;
import com.mostimes.haitao.entity.PmsProduct;
import com.mostimes.haitao.entity.PmsProductImage;
import com.mostimes.haitao.product.mapper.ProductCommentCountMapper;
import com.mostimes.haitao.product.mapper.ProductCommentMapper;
import com.mostimes.haitao.product.mapper.ProductImageMapper;
import com.mostimes.haitao.product.mapper.ProductMapper;
import com.mostimes.haitao.product.service.ProductService;
import com.mostimes.haitao.util.RedisUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ProductServiceImpl implements ProductService {
    @Autowired
    ProductMapper productMapper;
    @Autowired
    ProductImageMapper productImageMapper;
    @Autowired
    ProductCommentMapper productCommentMapper;
    @Autowired
    ProductCommentCountMapper productCommentCountMapper;
    @Autowired
    RedisUtil redisUtil;

    @Override
    //用id获取商品
    public PmsProduct getProductById(String productId) {
        Jedis jedis = null;
        PmsProduct pmsProduct;
        try{
            jedis = redisUtil.getJedis();
            if (jedis != null){
                String productStr = jedis.get("product:productId:" + productId + ":info");
                if (StringUtils.isNotBlank(productStr)){ //该商品存在redis
                    pmsProduct = JSON.parseObject(productStr, PmsProduct.class);
                }else { //该商品未在redis中，查询数据库
                    pmsProduct = getProductByIdFromDB(productId, 0);
                }
            }else { //开启redis失败，尝试从数据库查找
                pmsProduct = getProductByIdFromDB(productId, 1);
            }
            return pmsProduct;
        }finally {
            jedis.close();
        }
    }

    //从数据库中查找商品
    public PmsProduct getProductByIdFromDB(String productId, int cache){
        PmsProduct pmsProduct = productMapper.selectById(productId);
        if (cache == 0){
            Jedis jedis = null;
            try {
                jedis = redisUtil.getJedis();
                jedis.set("product:productId:" + pmsProduct.getId() + ":info",JSON.toJSONString(pmsProduct));
            }finally {
                jedis.close();
            }
        }

        return pmsProduct;
    }

    @Override
    //获取商品描述图片
    public PmsProductImage getProductImage(String productId) {
        Jedis jedis = null;
        PmsProductImage pmsProductImage;
        try{
            jedis = redisUtil.getJedis();
            if (jedis != null){
                String productImageStr = jedis.get("productImage:productId:" + productId + ":info");
                if (StringUtils.isNotBlank(productImageStr)){ //该商品存在redis
                    pmsProductImage = JSON.parseObject(productImageStr, PmsProductImage.class);
                }else { //该商品图片未在redis中，查询数据库
                    pmsProductImage = getProductImageFromDB(productId, 0);
                }
            }else { //开启redis失败，尝试从数据库查找
                pmsProductImage = getProductImageFromDB(productId, 1);
            }
            return pmsProductImage;
        }finally {
            jedis.close();
        }
    }

    //从数据中获取商品图片
    public PmsProductImage getProductImageFromDB(String productId, int cache){
        PmsProductImage pmsProductImage;
        Map<String, Object> map = new HashMap<>();
        map.put("product_id", productId);
        pmsProductImage = productImageMapper.selectByMap(map).get(0);
        if (cache == 0){
            Jedis jedis = null;
            try {
                jedis = redisUtil.getJedis();
                jedis.set("productImage:productId:" + productId + ":info",JSON.toJSONString(pmsProductImage));
            }finally {
                jedis.close();
            }
        }
        return pmsProductImage;
    }

    @Override
    //用商品id查找评价
    public List<PmsComment> getCommentByProductId(String productId, String page,String type) {
        int commentPage = Integer.valueOf(page).intValue();
        Jedis jedis = null;
        List<PmsComment> pmsCommentList = new ArrayList<>();
        try{
            jedis = redisUtil.getJedis();
            if (jedis != null){
                List<String> pmsCommentListStr;

                if ("3".equals(type)){
                    pmsCommentListStr = jedis.lrange( "comment:productId:" + productId + ":info", (commentPage - 1) * 10, (commentPage) * 10 - 1);
                }else {
                    pmsCommentListStr = jedis.lrange( "commentType:"+ type + ":" + productId + ":info",(commentPage - 1) * 10, (commentPage) * 10 - 1);
                }

                if (!pmsCommentListStr.isEmpty()){ // 该商品评价已存在redis
                    for (String str : pmsCommentListStr){
                        pmsCommentList.add(JSON.parseObject(str, PmsComment.class));
                    }
                }else { //评价未在redis中，查询数据库
                    pmsCommentList = getCommentByIdFromDB(productId,commentPage,0,type);
                }

            }else { //开启redis失败，尝试从数据库查找
                pmsCommentList = getCommentByIdFromDB(productId,commentPage,1,type);
            }
            return pmsCommentList;
        }finally {
            jedis.close();
        }
    }

    //从数据库中查找评价
    public List<PmsComment> getCommentByIdFromDB(String productId, int commentPage,int cache, String type){ //分页查询数据库中的商品评论
        List<PmsComment> pmsCommentList = new ArrayList<>();
        QueryWrapper<PmsComment> wrapper = new QueryWrapper<>();
        if ("3".equals(type)){
            wrapper.eq("product_id",productId);
        }else {
            wrapper.eq("product_id",productId).eq("Appraise",type);
        }
        Page<PmsComment> page = new Page<>(commentPage, 10);
        IPage<PmsComment> userIPage = productCommentMapper.selectPage(page, wrapper);
        List<PmsComment> list = userIPage.getRecords();
        for(PmsComment pmsComment : list){
            pmsCommentList.add(pmsComment);
        }

        if (cache == 0){
            List<PmsComment> commentList = new ArrayList<>();
            Jedis jedis = null;
            try {
                jedis = redisUtil.getJedis();
                if (jedis != null){
                    if ("3".equals(type)){
                        Map<String, Object> map = new HashMap<>();
                        map.put("product_id", productId);
                        commentList = productCommentMapper.selectByMap(map);
                        for (PmsComment pmsComment : commentList){
                            jedis.lpush("comment:productId:" + productId + ":info", JSON.toJSONString(pmsComment));
                        }
                    }else {
                        Map<String, Object> map = new HashMap<>();
                        map.put("product_id", productId);
                        map.put("Appraise", type);
                        commentList = productCommentMapper.selectByMap(map);
                        for (PmsComment pmsComment : commentList) {
                            jedis.lpush("commentType:"+ type + ":" + productId + ":info", JSON.toJSONString(pmsComment));
                        }
                    }
                }
            }finally {
                jedis.close();
            }
        }
        return pmsCommentList;
    }

    @Override
    //用商品id查找评价统计
    public PmsCommentCount getCommentCountByProductId(String productId) {
        Jedis jedis = null;
        PmsCommentCount pmsCommentCount;
        try{
            jedis = redisUtil.getJedis();
            if (jedis != null){
                String pmsCommentCountStr = jedis.get("commentCount:productId:" + productId +":info");
                if (StringUtils.isNotBlank(pmsCommentCountStr)){ // 该商品评论统计已存在redis
                    pmsCommentCount = JSON.parseObject(pmsCommentCountStr, PmsCommentCount.class);
                }else { //商品评价统计未在redis中，查询数据库
                    pmsCommentCount = getCommentCountByIdFromDB(productId, 0);
                }
            }else { //开启redis失败，尝试从数据库查找
                pmsCommentCount = getCommentCountByIdFromDB(productId, 1);

            }
            return pmsCommentCount;
        }finally {
            jedis.close();
        }
    }

    //从数据库中用商品id查找评价统计
    public PmsCommentCount getCommentCountByIdFromDB(String productId, int cache){
        Map<String,Object> map = new HashMap<>();
        map.put("product_id", productId);
        PmsCommentCount pmsCommentCount = productCommentCountMapper.selectByMap(map).get(0);
        if (cache == 0){
            Jedis jedis = null;
            try {
                jedis = redisUtil.getJedis();
                jedis.set("commentCount:productId:" + pmsCommentCount.getProductId() + ":info", JSON.toJSONString(pmsCommentCount));
            }finally {
                jedis.close();
            }
        }
        return pmsCommentCount;
    }
}
