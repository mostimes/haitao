package com.mostimes.haitao.user.controller;

import com.mostimes.haitao.annotaions.LoginRequired;
import com.mostimes.haitao.entity.OmsCart;
import com.mostimes.haitao.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin
public class UserCartController {
    @Autowired
    CartService cartService;

    @LoginRequired
    @RequestMapping(value = "/cart/addCart")
    public Boolean addCart(HttpServletRequest request, String productId,String quantity, String productPrice, String skuId){
        return cartService.addCart((String) request.getAttribute("id"),productId,quantity,productPrice,skuId);
    }

    @LoginRequired
    @RequestMapping(value = "/cart/getCartProduct")
    public List<OmsCart> getCartProduct(HttpServletRequest request){
        return cartService.getCartProduct((String) request.getAttribute("id"));
    }

    @LoginRequired
    @RequestMapping(value = "/cart/getCartProductAttr")
    public List<Map<String, String>> getCartProductAttr(String skuIdList){
        return cartService.getCartProductAttr(skuIdList);
    }

    @LoginRequired
    @RequestMapping(value = "/cart/deleteCartProduct")
    public Boolean getCartProductAttr(HttpServletRequest request,String cartId){
        return cartService.deleteCartProduct((String) request.getAttribute("id"),cartId);
    }

    @LoginRequired
    @RequestMapping(value = "/cart/fluctuateCartCount")
    public Boolean fluctuateCartCount(HttpServletRequest request,String cartId,String count){
        return cartService.fluctuateCartCount((String) request.getAttribute("id"),cartId,count);
    }
}
