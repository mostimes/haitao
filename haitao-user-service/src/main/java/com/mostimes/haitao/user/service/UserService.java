package com.mostimes.haitao.user.service;

import com.mostimes.haitao.entity.UmsMember;
import com.mostimes.haitao.entity.UmsMemberReceiveAddress;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public interface UserService {
    Map<String, Object> register(UmsMember umsMember);

    UmsMember login(String userName,String password,String type,String ip);

    UmsMember getPersonalInfo(String userId);

    Boolean upDataPersonalInfo(String userId,String nickName,String name, String birthday, String city, String gender, String job);

    List<UmsMemberReceiveAddress> getShipAddress(String userId);

    List<UmsMemberReceiveAddress> deleteShipAddress(String userId, String addressId);

    List<UmsMemberReceiveAddress> editShipAddress(String addressId, String userId, String name, String phone, String postCode, String detailAddress, String defaultStatus,String type);

    List<UmsMemberReceiveAddress> updateDefaultAddress(String userId,String oldAddressId,String newAddressId);
}
