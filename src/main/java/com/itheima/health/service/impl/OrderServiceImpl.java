package com.itheima.health.service.impl;

import com.itheima.health.constant.MessageConstant;
import com.itheima.health.dao.MemberDAO;
import com.itheima.health.dao.OrderDAO;
import com.itheima.health.dao.OrderSettingDAO;
import com.itheima.health.entity.Result;
import com.itheima.health.pojo.Member;
import com.itheima.health.pojo.Order;
import com.itheima.health.pojo.OrderSetting;
import com.itheima.health.service.OrderService;
import com.itheima.health.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;

@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    private OrderDAO orderDAO;
    @Autowired
    private MemberDAO memberDAO;
    @Autowired
    private OrderSettingDAO orderSettingDAO;

    /**
     * 体检预约服务完成预约
     * @param map 前端提交的预约参数
     * @return 统一响应结果，包含预约结果
     */
    @Override
    public Result order(Map<String, String> map) {
        // 1.获取请求参数,手机号,套餐id,下单日期
        String telephone = map.get("telephone");
        int setmealId = Integer.parseInt(map.get("setmealId"));
        String orderDateStr = map.get("orderDate");
        Date orderDate = null;
        try {
            orderDate = DateUtils.parseString2Date(orderDateStr);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.DATE_FORMAT_ERROR);
        }
        // 2.调用orderSettingDao，检查当前日期是否可以预约
        OrderSetting orderSetting = orderSettingDAO.findByOrderDate(orderDate);
        if (orderSetting == null) {
            // 3.没有设置，不能预约，返回 Result(false, MessageConstant.SELECTED_DATE_CANNOT_ORDER);
            return new Result(false, MessageConstant.SELECTED_DATE_CANNOT_ORDER);
        }

        // 4.检查预约日期是否预约已满
        if (orderSetting.getReservations() >= orderSetting.getNumber()) {
            // 5.预约已满，不能预约，返回 Result(false, MessageConstant.ORDER_FULL);
            return new Result(false, MessageConstant.ORDER_FULL);
        }

        // 6.调用memberDao，根据手机号检查当前用户是否为会员
        Member member = memberDAO.findByTelephone(telephone);
        // 7.当前用户不是会员，需要添加到会员表
        if (member == null) {
            member = new Member();
            member.setName(map.get("name"));
            member.setPhoneNumber(telephone);
            member.setIdCard(map.get("idCard"));
            member.setSex(map.get("sex"));
            member.setRegTime(new Date());
            // 8.调用memberDao，添加会员到数据库
            memberDAO.add(member);
        }

        // 9.可以预约，设置预约人数加一
        orderSetting.setReservations(orderSetting.getReservations() + 1);
        // 10.调用orderSettingDao根据日期更新预约人数
        orderSettingDAO.editReservationsByOrderDate(orderSetting);

        // 11.保存预约信息到预约表
        Order order = new Order(member.getId(), orderDate, map.get("orderType"), Order.ORDERSTATUS_NO, setmealId);
        orderDAO.add(order);

        // 12.预约成功，返回 Result(true,MessageConstant.ORDER_SUCCESS,order.getId());
        return new Result(true, MessageConstant.ORDER_SUCCESS, order.getId());
    }

    /**
     * 根据预约id查询预约信息，包括套餐信息和会员信息
     * @param id 预约id
     * @return 预约信息，使用Map保存前端需要的数据， member, setmeal, dorderDate, orderType
     */
    @Override
    public Map findById(Integer id) {
        // 1.调用DAO 根据预约id查询预约信息
        Map map = orderDAO.findFullOrderById(id);
        return map;
    }
}
