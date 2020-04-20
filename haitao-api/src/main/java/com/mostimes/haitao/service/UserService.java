package com.mostimes.haitao.service;

import com.mostimes.haitao.entity.UmsMember;
import com.mostimes.haitao.entity.UmsMemberReceiveAddress;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Map;

@FeignClient(value = "PROVIDER-USER")
public interface UserService {
    @RequestMapping(value = "/register/{nickName}/{tel}/{eMail}/{passWord}/{payPassWord}")
    Map<String, Object> register(@PathVariable("nickName") String nickName, @PathVariable("tel") String tel, @PathVariable("eMail") String eMail, @PathVariable("passWord") String passWord, @PathVariable("payPassWord") String payPassWord);

    @RequestMapping(value = "/login/{userName}/{password}/{type}/{ip}")
    UmsMember login(@PathVariable("userName") String userName, @PathVariable("password") String password, @PathVariable("type") String type,@PathVariable("ip") String ip);

    @RequestMapping(value = "/getPersonalInfo/{userId}")
    UmsMember getPersonalInfo(@PathVariable("userId") String userId);

    @RequestMapping(value = "/upDataPersonalInfo/{userId}/{nickName}/{name}/{birthday}/{city}/{gender}/{job}")
    Boolean upDataPersonalInfo(@PathVariable("userId") String userId,@PathVariable("nickName") String nickName,@PathVariable("name") String name,@PathVariable("birthday") String birthday,@PathVariable("city") String city,@PathVariable("gender") String gender,@PathVariable("job") String job);

    @RequestMapping(value = "/getShipAddress/{userId}")
    List<UmsMemberReceiveAddress> getShipAddress(@PathVariable("userId") String userId);

    @RequestMapping(value = "/deleteShipAddress/{userId}/{addressId}")
    List<UmsMemberReceiveAddress> deleteShipAddress(@PathVariable("userId") String userId, @PathVariable("addressId") String addressId);

    @RequestMapping(value = "/user/editShipAddress/{addressId}/{userId}/{name}/{phone}/{postCode}/{detailAddress}/{defaultStatus}/{type}")
    List<UmsMemberReceiveAddress> editShipAddress(@PathVariable("addressId") String addressId,@PathVariable("userId") String userId,@PathVariable("name") String name,@PathVariable("phone") String phone,@PathVariable("postCode") String postCode,@PathVariable("detailAddress") String detailAddress,@PathVariable("defaultStatus") String defaultStatus,@PathVariable("type") String type);

    @RequestMapping(value = "/updateDefaultAddress/{userId}/{oldAddressId}/{newAddressId}")
    List<UmsMemberReceiveAddress> updateDefaultAddress(@PathVariable("userId") String userId,@PathVariable("oldAddressId") String oldAddressId,@PathVariable("newAddressId") String newAddressId);
}
