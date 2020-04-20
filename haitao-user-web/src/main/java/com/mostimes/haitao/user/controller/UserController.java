package com.mostimes.haitao.user.controller;

import com.mostimes.haitao.annotaions.LoginRequired;
import com.mostimes.haitao.entity.UmsMember;
import com.mostimes.haitao.entity.UmsMemberReceiveAddress;
import com.mostimes.haitao.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin
public class UserController {
    @Autowired
    UserService userService;

    @RequestMapping(value = "/user/register")
    public Map<String, Object> register(String nickName, String tel, String eMail, String passWord, String payPassWord){
        return userService.register(nickName,tel,eMail,passWord,payPassWord);
    }

    @RequestMapping(value = "/user/login")
    public UmsMember login(String userName, String password, String type) {
        return userService.login(userName,password,type,"127.0.0.1");
    }

    @LoginRequired
    @RequestMapping(value = "/user/getPersonalInfo")
    public UmsMember getPersonalInfo(HttpServletRequest request) {
        return userService.getPersonalInfo((String) request.getAttribute("id"));
    }

    @LoginRequired
    @RequestMapping(value = "/user/upDataPersonalInfo")
    public Boolean upDataPersonalInfo(HttpServletRequest request,String nickName,String name, String birthday, String city, String gender, String job){
        return userService.upDataPersonalInfo((String) request.getAttribute("id"),nickName,name,birthday,city,gender,job);
    }

    @LoginRequired
    @RequestMapping(value = "/user/getShipAddress")
    public List<UmsMemberReceiveAddress> getShipAddress(HttpServletRequest request){
        return userService.getShipAddress((String) request.getAttribute("id"));
    }

    @LoginRequired
    @RequestMapping(value = "/user/deleteShipAddress")
    public List<UmsMemberReceiveAddress> deleteShipAddress(HttpServletRequest request,String addressId){
        return userService.deleteShipAddress((String) request.getAttribute("id"),addressId);
    }

    @LoginRequired
    @RequestMapping(value = "/user/editShipAddress")
    public List<UmsMemberReceiveAddress> editAddress(HttpServletRequest request,String addressId, String name, String phone, String postCode, String detailAddress, String defaultStatus, String type){
        return userService.editShipAddress(addressId, (String) request.getAttribute("id"),name,phone,postCode,detailAddress,defaultStatus,type);
    }

    @LoginRequired
    @RequestMapping(value = "/user/updateDefaultAddress")
    public List<UmsMemberReceiveAddress> updateDefaultAddress(HttpServletRequest request,String oldAddressId,String newAddressId){
        return userService.updateDefaultAddress((String) request.getAttribute("id"),oldAddressId,newAddressId);
    }
}
