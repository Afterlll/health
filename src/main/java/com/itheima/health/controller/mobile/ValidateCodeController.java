package com.itheima.health.controller.mobile;

import com.itheima.health.constant.MessageConstant;
import com.itheima.health.constant.RedisMessageConstant;
import com.itheima.health.entity.Result;
import com.itheima.health.utils.SMSUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

// 发送短信验证码
@RestController("/orderValidatecode")
@RequestMapping("/validatecode")
public class ValidateCodeController {
    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 体检预约时发送手机验证码
     * @param telephone 手机号
     * @return 统一响应结果
     */
    @RequestMapping("/send4Order.do")
    public Result send4Order(String telephone) {
        // 1. 生成四位随机数字
        String authCode = RandomStringUtils.randomNumeric(4);
        // 2. 发送短信
        String isOk = SMSUtils.send(telephone, authCode);
        // 3.判断短信是否发送成功? 是否为OK
        if ("OK".equals(isOk)) {
            // 3.1.短信发送成功，把验证码放到Redis中,存活时间5分钟
            ValueOperations<String, Object> ops = redisTemplate.opsForValue();
            ops.set(telephone + RedisMessageConstant.SENDTYPE_ORDER, authCode, 5, TimeUnit.MINUTES);

            // 3.2.返回统一响应结果，Result(true, MessageConstant.SEND_VALIDATECODE_SUCCESS);
            return new Result(true, MessageConstant.SEND_VALIDATECODE_SUCCESS);
        } else {
            // 3.3.短信发送失败，返回统一响应结果，Result(false, MessageConstant.SEND_VALIDATECODE_FAIL);
            return new Result(false, MessageConstant.SEND_VALIDATECODE_FAIL);
        }
    }

    /**
     * 手机快速登录时发送手机验证码
     * @param telephone 手机号
     * @return
     */
    @RequestMapping("/send4Login.do")
    public Result send4Login(String telephone) {
        // 1.生成4位数字验证码
        String authCode = RandomStringUtils.randomNumeric(4);

        // 2. 发送短信
        String isOk = SMSUtils.send(telephone, authCode);

//        String isOk = "OK"; // 模拟短信发送成功
//        System.out.println("发送短信验证码: " + isOk);
//        System.out.println("验证码为: " + authCode);

        // 3.判断短信是否发送成功? 是否为OK
        if ("OK".equals(isOk)) {
            // 3.1.短信发送成功，把验证码放到Redis中,存活时间5分钟
            ValueOperations<String, Object> ops = redisTemplate.opsForValue();
            ops.set(telephone + RedisMessageConstant.SENDTYPE_LOGIN, authCode, 5, TimeUnit.MINUTES);

            // 3.2.返回统一响应结果，Result(true, MessageConstant.SEND_VALIDATECODE_SUCCESS);
            return new Result(true, MessageConstant.SEND_VALIDATECODE_SUCCESS);
        } else {
            // 3.3.短信发送失败，返回统一响应结果，Result(false, MessageConstant.SEND_VALIDATECODE_FAIL);
            return new Result(false, MessageConstant.SEND_VALIDATECODE_FAIL);
        }
    }
}
