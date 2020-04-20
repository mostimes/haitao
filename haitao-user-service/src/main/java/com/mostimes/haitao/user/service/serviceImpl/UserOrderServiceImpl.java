package com.mostimes.haitao.user.service.serviceImpl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mostimes.haitao.entity.*;
import com.mostimes.haitao.user.mapper.UserCartMapper;
import com.mostimes.haitao.user.mapper.UserOrderItemMapper;
import com.mostimes.haitao.user.mapper.UserOrderMapper;
import com.mostimes.haitao.user.service.UserCartService;
import com.mostimes.haitao.user.service.UserOrderService;
import com.mostimes.haitao.user.service.UserService;
import com.mostimes.haitao.util.HttpUtils;
import com.mostimes.haitao.util.RedisUtil;
import com.mostimes.haitao.util.UUIDUtil;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;

import java.math.BigDecimal;
import java.util.*;

@Service
public class UserOrderServiceImpl implements UserOrderService {
    @Autowired
    UserOrderMapper userOrderMapper;
    @Autowired
    UserOrderItemMapper userOrderItemMapper;
    @Autowired
    UserCartService userCartService;
    @Autowired
    UserCartMapper userCartMapper;
    @Autowired
    UserService userService;
    @Autowired
    RedisUtil redisUtil;

    @Override
    //提交订单
    public int submitOrder(String userId, String cartIds, String addressId,String payType) {
        BigDecimal totalAmount = new BigDecimal(0);
        BigDecimal freightAmount = new BigDecimal(0);
        int result;
        Jedis jedis = null;
        String orderId = UUIDUtil.createId();

        OmsOrder omsOrder = new OmsOrder();
        List<UmsMemberReceiveAddress> addressList = userService.getShipAddress(userId);
        List<OmsCart> cartList = new ArrayList<>();
        OmsOrderItem orderItem = new OmsOrderItem();
        List<String> cartIdList = Arrays.asList(cartIds.split(","));

        for (String cartId : cartIdList){
            OmsCart cartFromDB = userCartMapper.selectById(cartId);
            orderItem.setId(UUIDUtil.createId());
            orderItem.setOrderId(orderId);
            orderItem.setProductCategoryId(cartFromDB.getProductCategoryId());
            orderItem.setProductId(cartFromDB.getProductId());
            orderItem.setProductName(cartFromDB.getProductName());
            orderItem.setProductPic(cartFromDB.getProductPic());
            orderItem.setProductPrice(cartFromDB.getProductPrice());
            orderItem.setProductQuantity(cartFromDB.getQuantity());
            orderItem.setProductSkuId(cartFromDB.getProductSku());
            orderItem.setProductAttr(cartFromDB.getProductAttr());
            userOrderItemMapper.insert(orderItem);

            try {
                jedis = redisUtil.getJedis();
                if (jedis != null){
                    jedis.lpush("orderItem:orderId:" + orderId + ":info", JSON.toJSONString(orderItem));
                }
            }finally {
                jedis.close();
            }
            userCartMapper.deleteById(cartId);
            totalAmount = totalAmount.add(cartFromDB.getProductPrice().multiply(new BigDecimal(cartFromDB.getQuantity())));
            freightAmount = cartFromDB.getProductFreight().compareTo(freightAmount) == 1 ? cartFromDB.getProductFreight() : freightAmount;
            cartList.add(cartFromDB);
        }

        Map<String,Object> map = new HashMap<>();
        map.put("member_id",userId);
        List<OmsCart> omsCartList = userCartMapper.selectByMap(map);
        if(omsCartList.size() != 0){
            userCartService.cacheCartProduct(omsCartList); //缓存购物车商品
        }else {
            try{
                jedis = redisUtil.getJedis();
                if (jedis != null){
                    jedis.del("cart:userId:" + userId + ":info");
                }
            }finally {
                jedis.close();
            }
        }

        for (UmsMemberReceiveAddress address : addressList){
            if (addressId.equals(address.getId())){
                omsOrder.setReceiveName(address.getName());
                omsOrder.setReceivePhone(address.getPhoneNumber());
                omsOrder.setReceiveAddress(address.getDetailAddress());
                omsOrder.setReceivePostcode(address.getPostCode());
                break;
            }
        }

        omsOrder.setId(orderId);
        omsOrder.setMemberId(userId);
        omsOrder.setProductQuantity(cartList.size());
        omsOrder.setCreateTime(new Date());
        omsOrder.setTotalAmount(totalAmount);
        omsOrder.setFreightAmount(freightAmount);
        omsOrder.setPayAmount(totalAmount.add(freightAmount));
        omsOrder.setPayType(Integer.parseInt(payType));
        omsOrder.setSourceType(0);
        omsOrder.setStatus(0);
        omsOrder.setAutoConfirmDay(14);
        omsOrder.setDeleteStatus(0);
        result = userOrderMapper.insert(omsOrder);
        return result;
    }

    @Override
    //用订单id获取订单详情
    public OmsOrder getOrderById(String orderId) {
        return userOrderMapper.selectById(orderId);
    }

    @Override
    //获取订单
    public List<OmsOrder> getOrder(String userId,String orderPage) {
        QueryWrapper<OmsOrder> wrapper = new QueryWrapper<>();
        wrapper.eq("member_id",userId).orderByDesc("create_time");

        Page<OmsOrder> page = new Page<>(Integer.valueOf(orderPage).intValue(), 10);
        IPage<OmsOrder> orderIPage = userOrderMapper.selectPage(page, wrapper);
        return orderIPage.getRecords();
    }

    @Override
    //获取订单商品
    public List<List<OmsOrderItem>> getOrderItem(String orderIdList) {
        List<List<OmsOrderItem>> orderLists = new ArrayList<>();
        List<String> orderIds = Arrays.asList(orderIdList.split(",")); //分割订单id
        Jedis jedis = null;
        try{
            jedis = redisUtil.getJedis();
            if (jedis != null){
                for (String orderId : orderIds){
                    List<OmsOrderItem> orderItems = new ArrayList<>();
                    List<String> orderStrList = jedis.lrange("orderItem:orderId:" + orderId + ":info",0,-1);
                    if (orderStrList.size() != 0){ //该订单商品已存在缓存
                        for (String orderStr : orderStrList) {
                            orderItems.add(JSON.parseObject(orderStr,OmsOrderItem.class));
                        }
                        orderLists.add(orderItems);
                    }else { //该订单商品未存在缓存，开启数据库
                        orderLists.add(getOrderItemFromDb(orderId,0));
                    }
                }
            }else { //开启缓存失败，尝试开启数据库
                for (String orderId : orderIds){
                    orderLists.add(getOrderItemFromDb(orderId,1));
                }
            }
        }finally {
            jedis.close();
        }
        return orderLists;
    }

    //从数据库中获取订单商品
    public List<OmsOrderItem> getOrderItemFromDb(String orderId, int cache){
        Map<String,Object> map = new HashMap<>();
        map.put("order_id",orderId);
        List<OmsOrderItem> orderItemList = userOrderItemMapper.selectByMap(map);
        if (cache == 0){
            Jedis jedis = null;
            try {
                jedis = redisUtil.getJedis();
                if (jedis != null){
                    for (OmsOrderItem orderItem : orderItemList){
                        jedis.lpush("orderItem:orderId:" + orderId + ":info",JSON.toJSONString(orderItem));
                    }
                }
            }finally {
                jedis.close();
            }
        }
        return orderItemList;
    }

    @Override
    //获取订单个数
    public int getOrderCount(String userId) {
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("member_id",userId);
        return userOrderMapper.selectCount(wrapper);
    }

    @Override
    //获取物流信息
    public String getLogisticsInfo(String code) {
        String host = "https://wuliu.market.alicloudapi.com";
        String path = "/kdi";
        String method = "GET";
        String appcode = "ac775f817cc74def86bfbaff0a487785";
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("Authorization", "APPCODE " + appcode);
        Map<String, String> querys = new HashMap<String, String>();
        querys.put("no", code);
        try {
            HttpResponse response = HttpUtils.doGet(host, path, method, headers, querys);
            return EntityUtils.toString(response.getEntity());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
