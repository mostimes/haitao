package com.mostimes.haitao;

import com.mostimes.haitao.util.JwtUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
public class HaitaoUserWebApplicationTests {

    @Test
    public void contextLoads() {
        Map<String, Object> decode = JwtUtil.decode("eyJhbGciOiJIUzI1NiJ9.eyJuaWNrbmFtZSI6IuWwj-W5uOi_kCIsImlkIjoiNDI3NjNiYmRmZTM0NDNiNmFkMmEwYjc4Zjg4ZWM1NzEifQ.SaFM9scBKVv92J2Au2aGoMM7Qv2TtBK2_AdGB4-DmfI", "2020haitao", "127.0.0.1");
        if (decode != null){
            System.out.println(decode.get("id"));
        }else {
            System.out.println("错误");
        }
    }

}
