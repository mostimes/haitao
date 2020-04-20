package com.mostimes.haitao.user.controller;

import com.alibaba.fastjson.JSON;
import com.mostimes.haitao.entity.UmsMember;
import com.mostimes.haitao.util.JwtUtil;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@CrossOrigin
public class PassportController {
    @RequestMapping("/verify")
    public String verify(String token, String currentIp){
        //通过jwt校验真假
        Map<String, String> map = new HashMap<>();
        Map<String, Object> decode = JwtUtil.decode(token, "2020haitao", currentIp);

        if (decode != null){
            map.put("status", "success");
            map.put("id", (String)decode.get("id"));
            map.put("nickname", (String)decode.get("nickname"));
        }else{
            map.put("status","fail");
        }

        return JSON.toJSONString(map);
    }

    @RequestMapping("/login")
    public String login(UmsMember umsMember){
        //调用用户服务验证用户密码
        return "token";
    }

    @RequestMapping("/index")
    public String index(String ReturnUrl, ModelMap map){
        map.put("ReturnUrl", ReturnUrl);
        return "登录页面";
    }
}
