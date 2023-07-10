package com.itheima.health;

import com.itheima.health.utils.SMSUtils;
import org.junit.jupiter.api.Test;

public class TestSMS {
    /**
     * 测试发短信
     */
    @Test
    void test01() {
        SMSUtils.send("15816709933", "666666");
    }
}
