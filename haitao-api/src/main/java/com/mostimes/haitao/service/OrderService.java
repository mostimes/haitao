package com.mostimes.haitao.service;

import com.mostimes.haitao.entity.OmsOrder;
import com.mostimes.haitao.entity.OmsOrderItem;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@FeignClient(value = "PROVIDER-USER")
public interface OrderService {
    @RequestMapping(value = "/submitOrder/{userId}/{cartIds}/{addressId}/{patType}")
    int submitOrder(@PathVariable("userId") String userId, @PathVariable("cartIds") String cartIds, @PathVariable("addressId") String addressId, @PathVariable("patType") String patType);

    @RequestMapping(value = "/getOrder/{userId}/{orderPage}")
    List<OmsOrder> getOrder(@PathVariable("userId") String userId, @PathVariable("orderPage") String orderPage);

    @RequestMapping(value = "/getOrderItem/{orderIdList}")
    List<List<OmsOrderItem>> getOrderItem(@PathVariable("orderIdList") String orderIdList);

    @RequestMapping(value = "/getOrderCount/{userId}")
    int getOrderCount(@PathVariable("userId") String userId);

    @RequestMapping(value = "/getOrderById/{orderId}")
    OmsOrder getOrderById(@PathVariable("orderId") String orderId);

    @RequestMapping(value = "/getLogisticsInfo/{code}")
    String getLogisticsInfo(@PathVariable("code") String code);
}
