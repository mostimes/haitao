package com.mostimes.haitao.user.controller;

import com.mostimes.haitao.annotaions.LoginRequired;
import com.mostimes.haitao.entity.OmsOrder;
import com.mostimes.haitao.entity.OmsOrderItem;
import com.mostimes.haitao.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@CrossOrigin
public class UserOrderController {
    @Autowired
    OrderService orderService;

    @LoginRequired
    @RequestMapping("/order/submitOrder")
    public int submitOrder(HttpServletRequest request,String cartIds,String addressId,String payType){
        return orderService.submitOrder((String) request.getAttribute("id"),cartIds,addressId,payType);
    }

    @LoginRequired
    @RequestMapping("/order/getOrder")
    public List<OmsOrder> getOrder(HttpServletRequest request, String orderPage){
        return orderService.getOrder((String) request.getAttribute("id"),orderPage);
    }

    @RequestMapping("/order/getOrderItem")
    public List<List<OmsOrderItem>> getOrderItem(String orderIdList){
        return orderService.getOrderItem(orderIdList);
    }

    @LoginRequired
    @RequestMapping("/order/getOrderCount")
    public int getOrderCount(HttpServletRequest request){
        return orderService.getOrderCount((String) request.getAttribute("id"));
    }

    @LoginRequired
    @RequestMapping("/order/getOrderById")
    public OmsOrder getOrderById(String orderId){
        return orderService.getOrderById(orderId);
    }

    @LoginRequired
    @RequestMapping("/order/getLogisticsInfo")
    public String getLogisticsInfo(String code){
        return orderService.getLogisticsInfo(code);
    }
}
