package com.itheima.health.controller.mobile;

import com.itheima.health.constant.MessageConstant;
import com.itheima.health.constant.RedisMessageConstant;
import com.itheima.health.entity.Result;
import com.itheima.health.pojo.Member;
import com.itheima.health.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/member")
public class MemberController {
    @Autowired
    private MemberService memberService;

    @Autowired
    private RedisTemplate redisTemplate;


    @RequestMapping("/login.do")
    public Result login(@RequestBody Map<String, String> map, HttpServletResponse response) {
        // 1.获取用户输入的手机号和验证码
        String telephone = map.get("telephone");
        String validateCode = map.get("validateCode");

        // 2.从Redis中获取缓存的验证码
        String authCode = (String) redisTemplate.opsForValue().get(telephone + RedisMessageConstant.SENDTYPE_LOGIN);

        // 3.如果Redis中没有验证码,提示用户验证码已过期
        if (authCode == null) {
            return new Result(false, MessageConstant.SEND_VALIDATECODE_OUTTIME);
        }
        // 4.如果验证码不正确，提示用户验证码不正确
        if (!authCode.equals(validateCode)) {
            return new Result(false, MessageConstant.VALIDATECODE_ERROR);
        }

        // 5.如果验证码正确，则调用DAO根据手机号查询Member会员
        Member member = memberService.findByTelephone(telephone);
        // 6.判断当前用户是否为会员，如果不是会员则自动完成会员注册
        if (member == null) {
            member = new Member();
            member.setPhoneNumber(telephone);
            member.setRegTime(new Date());
            memberService.add(member);
        }

        // 7.将会员信息保存到Redis，使用手机号作为key，保存时长为30分钟
        redisTemplate.opsForValue().set(telephone, member, 30, TimeUnit.MINUTES);

        // 8.响应统一结果
        return new Result(true, MessageConstant.LOGIN_SUCCESS);
    }
}