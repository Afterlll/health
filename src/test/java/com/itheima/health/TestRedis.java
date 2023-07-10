package com.itheima.health;

import com.itheima.health.pojo.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.util.concurrent.TimeUnit;

@SpringBootTest
public class TestRedis {

    @Autowired
    private RedisTemplate redisTemplate;

    // 测试Redis值为String时的操作
    @Test
    void test01() {
        // 获取操作字符串类型的对象
        ValueOperations ops = redisTemplate.opsForValue();
        // 添加数据
        ops.set("username", "wky");
        // 获取数据
        System.out.println("a ======== " + ops.get("username"));
        // 设置带过期时间的数据
        ops.set("yzm", "1256", 60, TimeUnit.SECONDS);
        // 获取数据
        System.out.println("yzm ======== " + ops.get("yzm"));

        // 存储对象
        User user = new User();
        user.setUsername("江喜原");
        user.setTelephone("15816709933");
        user.setPassword("123");
        ops.set("user", user);

        // 删除键值对
        System.out.println(redisTemplate.delete("username"));

        System.out.println(redisTemplate.expire("gender", 60, TimeUnit.SECONDS));

        System.out.println(redisTemplate.getExpire("gender"));
    }
}
