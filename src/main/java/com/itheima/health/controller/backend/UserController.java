package com.itheima.health.controller.backend;

import com.itheima.health.constant.MessageConstant;
import com.itheima.health.constant.RedisMessageConstant;
import com.itheima.health.entity.Result;
import com.itheima.health.pojo.User;
import com.itheima.health.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.concurrent.TimeUnit;

// 用户控制器
// 处理关于用户的一些浏览器请求和返回响应结果
@RestController
@RequestMapping("/user")
public class UserController {
    // 控制器需要依赖业务层
    @Autowired
    private UserService userService;
    @Autowired
    private RedisTemplate redisTemplate;

    @PostMapping("/register.do")
    /**
     * @RequestBody: 1. 取出请求体中的数据 2. 把JSON字符串转换为Java对象
     */
    private Result register(@RequestBody User registerUser) {
        // 1. 调用业务层进行注册
        Result result = userService.register(registerUser);
        // 2. 返回结果
        return result;
    }

    /**
     * 用户手机号和密码登录
     * @param loginUser
     * @return
     */
    @PostMapping("/login.do")
    private Result login(@RequestBody User loginUser) {
        // 1. 调用业务层进行登录
        Result result = userService.login(loginUser);
        // 2. 返回结果
        return result;
    }

    /**
     * 手机验证码登录
     * 请求参数：{“telephone” ：“15816709933”，“smsCode”：“1273”}
     * @param map
     * @return
     */
    @PostMapping("/telephoneLogin.do")
    private Result telephoneLogin(@RequestBody Map<String, String> map) {
        // 1.获取用户输入的手机号和验证码
        String telephone = map.get("telephone");
        String smsCode = map.get("smsCode");

        // 2.调用DAO根据手机号查询User
        User queryUser = userService.findByTelephone(telephone);

        // 3.如果手机号查询不到User,表示手机未注册,登录失败
        if (queryUser == null) {
            return new Result(false, "登录失败, 手机号不存在");
        }

        // 4.从Redis中获取缓存的验证码
        String authCode = (String) redisTemplate.opsForValue().get(telephone + RedisMessageConstant.SENDTYPE_LOGIN);

        // 5.如果Redis中没有验证码,提示用户验证码已过期
        if (authCode == null) {
            return new Result(false, MessageConstant.SEND_VALIDATECODE_OUTTIME);
        }

        // 6.如果验证码不正确，提示用户验证码不正确
        if (!authCode.equals(smsCode)) {
            return new Result(false, MessageConstant.VALIDATECODE_ERROR);
        }

        // 7.将User信息保存到Redis，使用手机号作为key，保存时长为30分钟
        redisTemplate.opsForValue().set(telephone, queryUser, 30, TimeUnit.MINUTES);

        // 8.删除Redis中的登录验证码
        redisTemplate.delete(telephone + RedisMessageConstant.SENDTYPE_LOGIN);

        // 9.响应统一结果
        return new Result(true, MessageConstant.LOGIN_SUCCESS);
    }
}
