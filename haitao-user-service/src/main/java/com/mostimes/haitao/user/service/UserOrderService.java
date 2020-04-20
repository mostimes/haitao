package com.mostimes.haitao.user.service;

import com.mostimes.haitao.entity.OmsOrder;
import com.mostimes.haitao.entity.OmsOrderItem;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserOrderService {
    int submitOrder(String userId,String cartIds,String addressId,String payType);

    List<OmsOrder> getOrder(String userId,String orderPage);

    OmsOrder getOrderById(String orderId);

    List<List<OmsOrderItem>> getOrderItem(String orderIdList);

    int getOrderCount(String userId);

    String getLogisticsInfo(String code);
}
