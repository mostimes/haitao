package com.mostimes.haitao.service;

import com.mostimes.haitao.entity.OmsCart;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Map;

@FeignClient(value = "PROVIDER-USER")
public interface CartService {
    @RequestMapping(value = "/addCart/{userId}/{productId}/{quantity}/{productPrice}/{skuId}")
    Boolean addCart(@PathVariable("userId") String userId, @PathVariable("productId") String productId, @PathVariable("quantity") String quantity, @PathVariable("productPrice") String productPrice, @PathVariable("skuId") String skuId);

    @RequestMapping(value = "/getCartProduct/{userId}")
    List<OmsCart> getCartProduct(@PathVariable("userId") String userId);

    @RequestMapping(value = "/getCartProductAttr/{skuIdList}")
    List<Map<String, String>> getCartProductAttr(@PathVariable("skuIdList") String skuIdList);

    @RequestMapping(value = "/deleteCartProduct/{userId}/{cartId}")
    Boolean deleteCartProduct(@PathVariable("userId") String userId,@PathVariable("cartId") String cartId);

    @RequestMapping(value = "/fluctuateCartCount/{userId}/{cartId}/{count}")
    Boolean fluctuateCartCount(@PathVariable("userId") String userId,@PathVariable("cartId") String cartId,@PathVariable("count") String count);
}
