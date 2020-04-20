package com.mostimes.haitao.user.service.serviceImpl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mostimes.haitao.entity.UmsMember;
import com.mostimes.haitao.entity.UmsMemberReceiveAddress;
import com.mostimes.haitao.user.mapper.UserAddressMapper;
import com.mostimes.haitao.user.mapper.UserMapper;
import com.mostimes.haitao.user.service.UserService;
import com.mostimes.haitao.util.JwtUtil;
import com.mostimes.haitao.util.MD5Util;
import com.mostimes.haitao.util.RedisUtil;
import com.mostimes.haitao.util.UUIDUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    UserMapper userMapper;
    @Autowired
    UserAddressMapper userAddressMapper;
    @Autowired
    RedisUtil redisUtil;

    @Override
    //注册
    public Map<String, Object> register(UmsMember umsMember) {
        Map<String, Object> map = checkTelEmailFromDB(umsMember); //检查昵称手机邮箱是否被注册
        if("200".equals(map.get("status"))) { //昵称手机邮箱未被注册
            Jedis jedis = null;
            userMapper.insert(umsMember); //插入数据库
            try {
                jedis = redisUtil.getJedis();
                if (jedis != null) {
                    //加入缓存
                    jedis.set("user:userId:" + umsMember.getId() + ":info", JSON.toJSONString(umsMember));
                }
            }finally {
                jedis.close();
            }
        }
        return map;
    }

    //从数据库中查询该昵称/手机/ 邮箱是否被注册
    public Map<String, Object> checkTelEmailFromDB(UmsMember umsMember) {
        Map<String, Object> map = new HashMap<>();
        QueryWrapper<UmsMember> nickname = new QueryWrapper<>();
        QueryWrapper<UmsMember> phone = new QueryWrapper<>();
        QueryWrapper<UmsMember> email = new QueryWrapper<>();
        nickname.eq("nickname", umsMember.getNickname());
        phone.eq("phone", umsMember.getPhone());
        email.eq("email", umsMember.getEmail());
        if (userMapper.selectCount(nickname) != 0) { //昵称被注册
            map.put("status", "500");
            map.put("msg", "该昵称已被注册");
            return map;
        } else if (userMapper.selectCount(phone) != 0) { //手机被注册
            map.put("status", "500");
            map.put("msg", "该手机已被注册");
            return map;
        }else if (userMapper.selectCount(email) != 0) { //邮箱被注册
            map.put("status", "500");
            map.put("msg", "该邮箱已被注册");
            return map;
        }
        map.put("status", "200");
        map.put("msg", "注册成功");
        return map;
    }

    @Override
    //登录
    public UmsMember login(String userName,String password,String type,String ip) {
        UmsMember umsMember = new UmsMember();
        Map<String, Object> map = new HashMap<>();
        if("0".equals(type)){ //手机号码登录
            map.put("phone", userName);
        }
        else { //邮箱登录
            map.put("email", userName);
        }

        List<UmsMember> umsMemberList = userMapper.selectByMap(map);
        if (umsMemberList.isEmpty()) { //用户不存在
            umsMember.setId("1");
            return umsMember;
        } else { //用户存在
            umsMember = umsMemberList.get(0);
            if (!MD5Util.md5(password).equals(umsMember.getPassword())){ //密码错误
                umsMember = new UmsMember();
                umsMember.setId("2");
            }else { //密码正确
                Map<String, Object> resultMap = new HashMap<>();
                resultMap.put("id", umsMember.getId());
                resultMap.put("nickname",umsMember.getNickname());
                umsMember.setToken(JwtUtil.encode("2020haitao", resultMap, "127.0.0.1"));
                Jedis jedis = null;
                try {
                    jedis = redisUtil.getJedis();
                    if(jedis != null) {
                        jedis.set("user:userId:" + umsMember.getId() + ":info", JSON.toJSONString(umsMember));
                    }
                }finally {
                    jedis.close();
                }
            }
        }
        return umsMember;
    }

    @Override
    //获取个人信息
    public UmsMember getPersonalInfo(String userId) {
        UmsMember umsMember = new UmsMember();
        Jedis jedis = null;
        try {
            jedis = redisUtil.getJedis();
            if (jedis != null) {
                String memberStr = jedis.get("user:userId:" + userId + ":info");
                if (StringUtils.isNotBlank(memberStr)) { //数据已存在缓存中
                    umsMember = JSON.parseObject(memberStr, UmsMember.class);
                }else { //缓存中未找到数据，开启数据库
                    umsMember =  getPersonalInfoFromDB(userId,0);
                }
            }else {//开启缓存失败，尝试开始数据库
                umsMember =  getPersonalInfoFromDB(userId,1);
            }
        }finally {
            jedis.close();
        }
        return umsMember;
    }

    //从数据库中获取个人信息
    public UmsMember getPersonalInfoFromDB(String userId, int type) {
        UmsMember umsMember = userMapper.selectById(userId);
        if (type == 0) {
            Jedis jedis = null;
            try {
                jedis = redisUtil.getJedis();
                if (jedis != null) {
                    jedis.set("user:userId:" + userId + ":info", JSON.toJSONString(umsMember));
                }
            }finally {
                jedis.close();
            }
        }
        return umsMember;
    }

    @Override
    //更新个人资料
    public Boolean upDataPersonalInfo(String userId, String nickName, String name, String birthday, String city, String gender, String job) {
        UmsMember umsMember = new UmsMember();
        umsMember.setId(userId);
        umsMember.setNickname(nickName);
        umsMember.setName(name);
        umsMember.setBirthday(birthday);
        umsMember.setCity(city);
        umsMember.setGender(gender);
        umsMember.setJob(job);

        int result = userMapper.updateById(umsMember);
        if (result == 1) {
            umsMember = userMapper.selectById(umsMember.getId());
            Jedis jedis = null;
            try {
                jedis = redisUtil.getJedis();
                if (jedis != null){
                    jedis.set("user:userId:" + umsMember.getId() + ":info", JSON.toJSONString(umsMember));
                }
            }finally {
                jedis.close();
            }
            return true;
        }
        return false;
    }

    @Override
    //获取用户地址
    public List<UmsMemberReceiveAddress> getShipAddress(String userId) {
        List<UmsMemberReceiveAddress> addressList = new ArrayList<>();
        Jedis jedis = null;
        try {
            jedis = redisUtil.getJedis();
            if (jedis != null){
                List<String> addressStr = jedis.lrange("shipAddress:userId:" + userId + ":info",0,-1);
                if(!addressStr.isEmpty()) { //该数据已存在缓存中
                    for (String address : addressStr) {
                        addressList.add(JSON.parseObject(address, UmsMemberReceiveAddress.class));
                    }
                }else { //该用户地址数据未存在redis，开始数据库
                    addressList = getShipAddressFromDB(userId,0);
                }
            }else { //开始redis失败，尝试开启数据库
                addressList = getShipAddressFromDB(userId,1);
            }
        }finally {
            jedis.close();
        }
        return addressList;
    }

    //从数据库中获取用户地址
    public List<UmsMemberReceiveAddress> getShipAddressFromDB(String userId, int cache) {
        List<UmsMemberReceiveAddress> addressList;
        Map<String, Object> map = new HashMap<>();
        map.put("member_id", userId);
        addressList = userAddressMapper.selectByMap(map);
        if (cache == 0){
            cacheAddress(addressList);
        }
        return addressList;
    }

    @Override
    //删除地址
    public List<UmsMemberReceiveAddress> deleteShipAddress(String userId, String addressId) {
        Jedis jedis = null;
        UmsMemberReceiveAddress receiveAddress;
        List<UmsMemberReceiveAddress> addressList;
        try {
            jedis = redisUtil.getJedis();
            if (jedis != null){
                List<String> addressStr = jedis.lrange("shipAddress:userId:" + userId + ":info",0,-1);
                if (!addressStr.isEmpty()){ //缓存中有数据
                    jedis.del("shipAddress:userId:" + userId + ":info");
                    for (String address : addressStr){
                        receiveAddress = JSON.parseObject(address, UmsMemberReceiveAddress.class);
                        if (!addressId.equals(receiveAddress.getId())){
                            jedis.lpush("shipAddress:userId:" + userId + ":info",JSON.toJSONString(receiveAddress));
                        }
                    }
                    addressList = deleteShipAddressFromDB(userId,addressId,1);
                }else { //缓存中没有数据
                    addressList = deleteShipAddressFromDB(userId,addressId,0);
                }
            }else {
                addressList = deleteShipAddressFromDB(userId,addressId,1);
            }
            return addressList;
        }finally {
            jedis.close();
        }
    }

    //删除数据库中的指定地址
    public List<UmsMemberReceiveAddress> deleteShipAddressFromDB(String userId, String addressId, int cache){
        List<UmsMemberReceiveAddress> addressList;
        userAddressMapper.deleteById(addressId);
        Map<String,Object> map = new HashMap<>();
        map.put("member_id", userId);
        addressList = userAddressMapper.selectByMap(map);
        if (cache == 0){
            cacheAddress(addressList);
        }
        return addressList;
    }

    @Override
    //编辑或添加地址
    public List<UmsMemberReceiveAddress> editShipAddress(String addressId, String userId, String name, String phone, String postCode, String detailAddress, String defaultStatus, String type) {
        List<UmsMemberReceiveAddress> receiveAddressList;
        UmsMemberReceiveAddress receiveAddress = new UmsMemberReceiveAddress();
        if ("0".equals(type)){
            receiveAddress.setId(UUIDUtil.createId());
        }else {
            receiveAddress.setId(addressId);
        }
        receiveAddress.setDefaultStatus(defaultStatus);
        receiveAddress.setMemberId(userId);
        receiveAddress.setName(name);
        receiveAddress.setPhoneNumber(phone);
        receiveAddress.setPostCode(postCode);
        receiveAddress.setDetailAddress(detailAddress);
        Jedis jedis = null;
        try {
            jedis = redisUtil.getJedis();
            if (jedis != null){
                List<String> addressStr =  jedis.lrange("shipAddress:userId:" + userId + ":info",0,-1);
                if (!addressStr.isEmpty()){
                    if ("0".equals(type)){ //添加地址
                        jedis.lpush("shipAddress:userId:" + userId + ":info",JSON.toJSONString(receiveAddress));
                        receiveAddressList = editShipAddressFromDB(receiveAddress,type,1);
                    }else { //编辑地址
                        jedis.del("shipAddress:userId:" + userId + ":info");
                        for (String address : addressStr){
                            if (addressId.equals(JSON.parseObject(address, UmsMemberReceiveAddress.class).getId())){
                                jedis.lpush("shipAddress:userId:" + userId + ":info",JSON.toJSONString(receiveAddress));
                            }else {
                                jedis.lpush("shipAddress:userId:" + userId + ":info", address);
                            }
                        }
                        receiveAddressList = editShipAddressFromDB(receiveAddress,type,1);
                    }
                }else {
                    receiveAddressList = editShipAddressFromDB(receiveAddress,type,0);
                }
            }else {
                receiveAddressList = editShipAddressFromDB(receiveAddress,type,1);
            }
            return receiveAddressList;
        }finally {
            jedis.close();
        }
    }

    //编辑或添加数据库中的地址
    public List<UmsMemberReceiveAddress> editShipAddressFromDB(UmsMemberReceiveAddress address, String type, int cache){
        if ("0".equals(type)){ //添加进数据库
            userAddressMapper.insert(address);
        }else { //更新数据库
            userAddressMapper.updateById(address);
        }
        Map<String, Object> map = new HashMap<>();
        map.put("member_id",address.getMemberId());
        List<UmsMemberReceiveAddress> addressList = userAddressMapper.selectByMap(map);
        if(cache == 0){
            cacheAddress(addressList);
        }
        return addressList;
    }

    @Override
    //修改默认地址
    public List<UmsMemberReceiveAddress> updateDefaultAddress(String userId, String oldAddressId, String newAddressId) {
        List<UmsMemberReceiveAddress> receiveAddressList;
        Jedis jedis = null;
        try {
            jedis = redisUtil.getJedis();
            if (jedis != null){
                List<String> addressStr = jedis.lrange("shipAddress:userId:" + userId + ":info",0,-1);
                if (!addressStr.isEmpty()){
                    jedis.del("shipAddress:userId:" + userId + ":info");
                    for (String address : addressStr){
                        UmsMemberReceiveAddress receiveAddress = JSON.parseObject(address,UmsMemberReceiveAddress.class);
                        if (oldAddressId.equals(receiveAddress.getId())){
                            receiveAddress.setDefaultStatus("1");
                        }else if(newAddressId.equals(receiveAddress.getId())){
                            receiveAddress.setDefaultStatus("0");
                        }
                        jedis.lpush("shipAddress:userId:" + userId + ":info",JSON.toJSONString(receiveAddress));
                    }
                    receiveAddressList = updateDefaultAddressFromDB(userId, oldAddressId, newAddressId, 1);
                }else {
                    receiveAddressList = updateDefaultAddressFromDB(userId, oldAddressId, newAddressId, 0);
                }
            }else {
                receiveAddressList = updateDefaultAddressFromDB(userId, oldAddressId, newAddressId, 1);
            }
        }finally {
            jedis.close();
        }
        return receiveAddressList;
    }

    //修改数据库中的默认地址
    public List<UmsMemberReceiveAddress> updateDefaultAddressFromDB(String userId, String oldAddressId, String newAddressId,int cache){
        List<UmsMemberReceiveAddress> receiveAddressList;

        UmsMemberReceiveAddress address = new UmsMemberReceiveAddress();
        address.setId(oldAddressId);
        address.setDefaultStatus("1");
        userAddressMapper.updateById(address);

        address.setId(newAddressId);
        address.setDefaultStatus("0");
        userAddressMapper.updateById(address);

        Map<String, Object> map = new HashMap<>();
        map.put("member_id", userId);
        receiveAddressList = userAddressMapper.selectByMap(map);
        if (cache == 0){
            cacheAddress(receiveAddressList);
        }
        return receiveAddressList;
    }

    //缓存用户地址
    void cacheAddress(List<UmsMemberReceiveAddress> addressStr){
        Jedis jedis = null;
        try {
            jedis = redisUtil.getJedis();
            if (jedis != null){
                for (UmsMemberReceiveAddress address : addressStr){
                    jedis.lpush("shipAddress:userId:" + address.getMemberId() + ":info", JSON.toJSONString(address));
                }
            }
        }finally {
            jedis.close();
        }
    }
}
