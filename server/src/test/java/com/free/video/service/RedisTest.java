package com.free.video.service;

import org.junit.Test;
import redis.clients.jedis.Jedis;

import java.util.UUID;

public class RedisTest {


    @Test
    public void testRedis() {
        Jedis jedis = null;
        try {
            // 1. 设置IP地址和端口
            jedis = new Jedis("192.168.122.22",6379);
            // 2. 保存数据
//        jedis.set("name","imooc");
//        // 3. 获取数据
//        String value = jedis.get("name");
//        System.out.println(value);
            while (true) {
                jedis.hset("ids", UUID.randomUUID().toString(), "1");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 4.释放资源
            jedis.close();
        }



    }

}
