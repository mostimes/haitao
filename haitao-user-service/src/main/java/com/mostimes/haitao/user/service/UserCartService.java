package com.mostimes.haitao.user.service;

import com.mostimes.haitao.entity.OmsCart;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public interface UserCartService {
    Boolean addCart(String userId, String productId,String quantity, String productPrice, String skuId);

    List<OmsCart> getCartProduct(String userId);

    List<Map<String, String>> getCartProductAttr(String skuIdList);

    Boolean deleteCartProduct(String userId,String cartId);

    Boolean fluctuateCartCount(String userId,String cartId,String count);

    void cacheCartProduct(List<OmsCart> cartList);
}
