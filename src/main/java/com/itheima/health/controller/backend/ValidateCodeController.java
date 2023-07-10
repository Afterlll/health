package com.itheima.health.controller.backend;

import com.itheima.health.constant.MessageConstant;
import com.itheima.health.constant.RedisMessageConstant;
import com.itheima.health.entity.Result;
import com.itheima.health.utils.SMSUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/validatecode")
public class ValidateCodeController {

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 后台手机快速登录时发送手机验证码
     * @param telephone
     * @return
     */
    @GetMapping("/backendSend4Login.do")
    public Result backendSend4Login(String telephone) {
        // 1. 生成随机四位验证码
        String autoCode = RandomStringUtils.randomNumeric(4);

        // 2. 调用阿里云工具类发送短信
//        String isOk = SMSUtils.send(telephone, autoCode);

        // 模拟发送短信
        String isOk = "OK";
        System.out.println("短信验证码：" + autoCode);

        // 3. 判断短信是否发送成功？成功时为OK
        if ("OK".equals(isOk)) {
            // 3.1 短信发送成功，把验证码放到Redis中，存活时间为5分钟
            redisTemplate.opsForValue().set(telephone + RedisMessageConstant.SENDTYPE_LOGIN, autoCode, 5, TimeUnit.MINUTES);
            // 3.2 返回统一响应结果，Result(true, MessageConstant.SEND_VALIDATECODE_SUCCESS);
            return new Result(true, MessageConstant.SEND_VALIDATECODE_SUCCESS);
        } else {
            // 3.3 短信发送失败，返回统一响应结果，Result(false, MessageConstant.SEND_VALIDATECODE_FAIL);
            return new Result(false, MessageConstant.SEND_VALIDATECODE_FAIL);
        }
    }
}
