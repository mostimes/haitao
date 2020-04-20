package com.mostimes.haitao.util;

import java.util.UUID;

public class UUIDUtil {
    /**
     * 利用U生成主键UID算法
     * @return
     */
    public static String createId(){
        UUID uuid = UUID.randomUUID();
        return uuid.toString().replace("-", "");
    }

}
