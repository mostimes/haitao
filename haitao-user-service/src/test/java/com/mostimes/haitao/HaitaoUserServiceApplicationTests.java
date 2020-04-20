package com.mostimes.haitao;

import com.mostimes.haitao.user.mapper.UserCartMapper;
import com.mostimes.haitao.user.mapper.UserOrderMapper;
import com.mostimes.haitao.util.MD5Util;
import com.mostimes.haitao.util.RedisUtil;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;


@RunWith(SpringRunner.class)
@SpringBootTest
public class HaitaoUserServiceApplicationTests {
    @Autowired
    RedisUtil redisUtil;
    @Autowired
    UserCartMapper userCartMapper;
    @Autowired
    UserOrderMapper userOrderMapper;

    @Test
    public void contextLoads() {
        System.out.println(MD5Util.md5("970325"));
    }

}
