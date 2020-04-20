package com.mostimes.haitao.user.service.serviceImpl;

import com.alibaba.fastjson.JSON;
import com.mostimes.haitao.entity.*;
import com.mostimes.haitao.user.mapper.ProductMapper;
import com.mostimes.haitao.user.mapper.ProductSkuSaleAttrValueMapper;
import com.mostimes.haitao.user.mapper.UserCartMapper;
import com.mostimes.haitao.user.service.UserCartService;
import com.mostimes.haitao.util.RedisUtil;
import com.mostimes.haitao.util.UUIDUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;

import java.math.BigDecimal;
import java.util.*;

import static jdk.nashorn.internal.objects.NativeDebug.map;

@Service
public class UserCartServiceImpl implements UserCartService {
    @Autowired
    UserCartMapper userCartMapper;
    @Autowired
    ProductMapper productMapper;
    @Autowired
    ProductSkuSaleAttrValueMapper productSkuSaleAttrValueMapper;
    @Autowired
    RedisUtil redisUtil;

    @Override
    //加入购物车
    public Boolean addCart(String userId, String productId,String quantity, String productPrice, String skuId) {
        PmsProduct pmsProduct;
        pmsProduct = productMapper.selectById(productId);
        Map<String,Object> map = new HashMap<>();
        map.put("sku_id",skuId);

        OmsCart cart = new OmsCart();
        cart.setId(UUIDUtil.createId());
        cart.setMemberId(userId);
        cart.setProductId(productId);
        cart.setQuantity(Integer.parseInt(quantity));
        cart.setProductPrice(new BigDecimal(productPrice));
        cart.setProductPic(pmsProduct.getProductMainPic());
        cart.setProductName(pmsProduct.getProductName());
        cart.setProductSku(skuId);
        cart.setProductFreight(pmsProduct.getProductFreight());
        cart.setProductCategoryId(pmsProduct.getProductCategoryId());
        cart.setCreateDate(new Date());
        Map<String,String> attrMap = new HashMap<>();
        List<Map<String,String>> attrMapList = new ArrayList<>();
        for (PmsSkuSaleAttrValue attrValue : productSkuSaleAttrValueMapper.selectByMap(map)){
            attrMap.put(attrValue.getSaleAttrName(),attrValue.getSaleAttrValueName());
        }
        attrMapList.add(attrMap);
        cart.setProductAttr(JSON.toJSONString(attrMapList));
        cart.setSelectStatus(0);
        Boolean result;
        Jedis jedis = null;
        try {
            jedis = redisUtil.getJedis();
            if (jedis != null){
                List<String> cartStrList = jedis.lrange("cart:userId:" + userId + ":info",0,-1);
                if (!cartStrList.isEmpty()){
                    jedis.del("cart:userId:" + userId + ":info");
                    int count = 0;
                    for (String cartStr : cartStrList){
                        OmsCart cartCache = JSON.parseObject(cartStr,OmsCart.class);
                        if (cartCache.getProductSku().equals(skuId) && cartCache.getProductId().equals(productId)){
                            int quantityTemp = cartCache.getQuantity() + Integer.parseInt(quantity);
                            cartCache.setQuantity(quantityTemp);
                            count++;
                            jedis.lpush("cart:userId:" + userId + ":info", JSON.toJSONString(cartCache));
                        }else {
                            jedis.lpush("cart:userId:" + userId + ":info", JSON.toJSONString(cartCache));
                        }
                    }
                    if (count == 0) {
                        jedis.lpush("cart:userId:" + userId + ":info", JSON.toJSONString(cart));
                    }
                    result = addCartFromDB(cart,1);
                }else{
                    result = addCartFromDB(cart,0);
                }
            }else {
                result = addCartFromDB(cart,1);
            }
        }finally {
            jedis.close();
        }
        return result;
    }

    //向数据库加入购物车商品
    public Boolean addCartFromDB(OmsCart omsCart,int cache){
        int insertResult = 0;
        Map<String,Object> map = new HashMap<>();
        map.put("member_id",omsCart.getMemberId());
        map.put("product_id",omsCart.getProductId());
        map.put("product_sku",omsCart.getProductSku());
        List<OmsCart> omsCartList = userCartMapper.selectByMap(map);
        if (omsCartList.isEmpty()){
            insertResult = userCartMapper.insert(omsCart);
        }else {
            omsCart.setId(omsCartList.get(0).getId());
                int quantityTemp = omsCartList.get(0).getQuantity() + omsCart.getQuantity();
            omsCart.setQuantity(quantityTemp);
            insertResult = userCartMapper.updateById(omsCart);
        }

        if (cache == 0){
            Map<String, Object> cartMap = new HashMap<>();
            cartMap.put("member_id",omsCart.getMemberId());
            cacheCartProduct(userCartMapper.selectByMap(cartMap));
        }

        if (insertResult == 1){
            return true;
        }else {
            return false;
        }
    }

    @Override
    //获取购物车商品
    public List<OmsCart> getCartProduct(String userId) {
        List<OmsCart> cartList = new ArrayList<>();
        Jedis jedis = null;
        try {
            jedis = redisUtil.getJedis();
            if (jedis != null){
                List<String> cartProductStrList = jedis.lrange("cart:userId:" + userId + ":info",0,-1);
                if (!cartProductStrList.isEmpty()){ //该用户购物车数据已存在缓存中
                    for (String cartProduct : cartProductStrList){
                        cartList.add(JSON.parseObject(cartProduct, OmsCart.class));
                    }
                }else { //该用户购物车数据未已存在缓存中，开启数据库
                    cartList = getCartProductFromDB(userId,0);
                }
            }else{ //开启缓存失败，尝试开启数据库
                cartList = getCartProductFromDB(userId,1);
            }
        }finally {
            jedis.close();
        }
        return cartList;
    }

    //从数据库中获取购物车商品
    public List<OmsCart> getCartProductFromDB(String userId,int cache){
        List<OmsCart> cartList = new ArrayList<>();
        Map<String,Object> map = new HashMap<>();
        map.put("member_id",userId);
        cartList = userCartMapper.selectByMap(map);

        if (cache == 0){
            cacheCartProduct(cartList);
        }
        return cartList;
    }

    @Override
    //获取购物车商品属性
    public List<Map<String, String>> getCartProductAttr(String skuIdList) {
        List<Map<String, String>> attrList= new ArrayList<>();
        List<PmsSkuSaleAttrValue> attrValueList = new ArrayList<>();
        List<String> skuIds = Arrays.asList(skuIdList.split(","));
        for (String skuId : skuIds){
            Map<String, Object> map = new HashMap<>();
            Map<String, String> attrMap = new HashMap<>();
            map.put("sku_id", skuId);
            attrValueList = productSkuSaleAttrValueMapper.selectByMap(map);
            for (PmsSkuSaleAttrValue skuSaleAttrValue : attrValueList){
                attrMap.put(skuSaleAttrValue.getSaleAttrName(),skuSaleAttrValue.getSaleAttrValueName());
            }
            attrList.add(attrMap);
        }
        return attrList;
    }

    @Override
    //删除购物车商品
    public Boolean deleteCartProduct(String userId, String cartId) {
        Jedis jedis = null;
        try {
            jedis = redisUtil.getJedis();
            if (jedis != null){
                List<String> cartStrList = jedis.lrange("cart:userId:" + userId + ":info",0,-1);
                if (!cartStrList.isEmpty()){
                    jedis.del("cart:userId:" + userId + ":info");
                    for (String cartStr : cartStrList){
                        if (!JSON.parseObject(cartStr,OmsCart.class).getId().equals(cartId)){
                            jedis.lpush("cart:userId:" + userId + ":info",cartStr);
                        }
                    }
                    return deleteCartProductFromDB(userId,cartId,1);
                }else{
                    return deleteCartProductFromDB(userId,cartId,0);
                }
            }else {
                return deleteCartProductFromDB(userId,cartId,1);
            }
        }finally {
            jedis.close();
        }
    }

    //删除数据库中购物车商品
    public Boolean deleteCartProductFromDB(String userId,String cartId, int type){
        int result = userCartMapper.deleteById(cartId);

        if (type == 0){
            Map<String, Object> map = new HashMap<>();
            map("member_id", userId);
            cacheCartProduct(userCartMapper.selectByMap(map));
        }
        if (result == 1){
            return true;
        }else {
            return false;
        }
    }

    @Override
    //更改购物车商品数量
    public Boolean fluctuateCartCount(String userId,String cartId,String count) {
        Jedis jedis = null;
        try {
            jedis = redisUtil.getJedis();
            if (jedis != null){
                List<String> cartStrList = jedis.lrange("cart:userId:" + userId + ":info",0,-1);
                if (!cartStrList.isEmpty()){
                    jedis.del("cart:userId:" + userId + ":info");
                    for (String cart : cartStrList){
                        OmsCart omsCart = JSON.parseObject(cart, OmsCart.class);
                        if (cartId.equals(omsCart.getId())){
                            omsCart.setQuantity(Integer.parseInt(count));
                            jedis.lpush("cart:userId:" + userId + ":info",JSON.toJSONString(omsCart));
                        }else {
                            jedis.lpush("cart:userId:" + userId + ":info",JSON.toJSONString(omsCart));
                        }
                    }
                    return fluctuateCartCountFromDB(userId,cartId,count,1);
                }else{
                    return fluctuateCartCountFromDB(userId,cartId,count,0);
                }
            }else {
                return fluctuateCartCountFromDB(userId,cartId,count,1);
            }
        }finally {
            jedis.close();
        }
    }

    //更改数据库中购物车商品数量
    public Boolean fluctuateCartCountFromDB(String userId,String cartId,String count, int cache){
        OmsCart cart = new OmsCart();
        cart.setId(cartId);
        cart.setQuantity(Integer.parseInt(count));
        int result = userCartMapper.updateById(cart);

        if (cache == 0){
            Map<String, Object> map = new HashMap<>();
            map.put("member_id",userId);
            cacheCartProduct(userCartMapper.selectByMap(map));
        }

        if (result == 1){
            return true;
        }else {
            return false;
        }
    }

    @Override
    //缓存购物车商品
    public void cacheCartProduct(List<OmsCart> cartList){
        Jedis jedis = null;
        try {
            jedis = redisUtil.getJedis();
            if (jedis != null){
                jedis.del("cart:userId:" +cartList.get(0).getMemberId() + ":info");
                for (OmsCart cart : cartList){
                    jedis.lpush("cart:userId:" + cart.getMemberId() + ":info",JSON.toJSONString(cart));
                }
            }
        }finally {
            jedis.close();
        }
    }
}
