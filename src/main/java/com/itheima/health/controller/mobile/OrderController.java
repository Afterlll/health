package com.itheima.health.controller.mobile;

import com.itheima.health.constant.MessageConstant;
import com.itheima.health.constant.RedisMessageConstant;
import com.itheima.health.entity.Result;
import com.itheima.health.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * 用户预约控制器
 * 用户可以通过如下操作流程进行体检预约：
 *  1、在移动端首页点击体检预约，页面跳转到套餐列表页面
 *  2、在套餐列表页面点击要预约的套餐，页面跳转到套餐详情页面
 *  3、在套餐详情页面点击立即预约，页面跳转到预约页面
 *  4、在预约页面录入体检人信息，包括手机号，点击发送验证码
 *  5、在预约页面录入收到的手机短信验证码，点击提交预约，完成体检预约
 */
@RestController
@RequestMapping("/order")
public class OrderController {
    @Autowired
    private OrderService orderService;
    @Autowired
    private RedisTemplate redisTemplate;
    @PostMapping("/submitOrder.do")
    public Result submitOrder(@RequestBody Map<String, String> map) {
        // 1.前端提交的验证码
        String validateCode = map.get("validateCode");

        // 2.前端提交的手机号
        String telephone = map.get("telephone");

        // 3.获取Redis中的验证码
        String authCode = (String) redisTemplate.opsForValue().get(telephone + RedisMessageConstant.SENDTYPE_ORDER);

        // 4.校验短信验证码
        if (authCode == null || !authCode.equals(validateCode)) {
            return new Result(false, MessageConstant.VALIDATECODE_ERROR);
        }

        // 5.调用DAO务完成预约
        Result result = orderService.order(map);

        return result;
    }

    /**
     * 根据预约id查询预约信息，包括套餐信息和会员信息
     * @param id 预约id
     * @return 预约信息，使用Map保存前端需要的数据， member, setmeal, dorderDate, orderType
     */
    @RequestMapping("/findById.do")
    public Result findById(Integer id){
        // 调用DAO根据预约id查询预约信息
        Map map = orderService.findById(id);

        return new Result(true,MessageConstant.QUERY_ORDER_SUCCESS, map);
    }
}
