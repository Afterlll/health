package com.itheima.health.service.impl;

import com.itheima.health.dao.OrderSettingDAO;
import com.itheima.health.pojo.OrderSetting;
import com.itheima.health.service.OrderSettingService;
import com.itheima.health.utils.DateUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
@Slf4j // 日志

public class OrderSettingServiceImpl implements OrderSettingService {

    @Autowired
    private OrderSettingDAO orderSettingDAO;

    /**
     * 添加预约设置对象
     * 将上传文件中的内容读取到数据库中
     * 创建服务实现类OrderSettingServiceImpl并实现新增方法，步骤如下：
     *      1. 遍历得到每一行数据
     *      2. 从数据库查询此日期是否已经有设置可预约数量
     *      3. 如果此日期有设置可预约数量就更新
     *      4. 如果此日期没有设置可预约数量就添加
     * @param orderSettingList
     */
    @Override
    @Transactional
    public void add(List<OrderSetting> orderSettingList) {
        // 1. 遍历得到每一行数据
        for (OrderSetting orderSetting : orderSettingList) {
            // 2. 从数据库查询此日期是否已经有设置可预约数量
            OrderSetting dbOrderSetting = orderSettingDAO.findByOrderDate(orderSetting.getOrderDate());
            if (dbOrderSetting != null) {
                // 3. 如果此日期有设置可预约数量就更新
                log.info("此日期有设置可预约数量就更新: {}", orderSetting.getNumber());
                orderSettingDAO.editNumberByOrderDate(orderSetting);
            } else {
                // 4. 如果此日期没有设置可预约数量就添加
                log.info("此日期没有设置可预约数量就添加: {}", orderSetting.getNumber());
                orderSettingDAO.add(orderSetting);
            }
        }
    }

    /**
     * 根据日期查询预约设置数据(获取指定日期所在月份的预约设置数据)
     * @param month 月份，格式为：2023-6
     * @return 当月的预约设置数据
     */
    @Override
    public List<OrderSetting> getOrderSettingByMonth(Date month) {
        // 1. 通过Calendar对象得到本月第一天的日期
        Date begin = DateUtils.getFirstDay4Date(month);

        // 2. 通过Calendar对象得到本月最后一天的日期
        Date end = DateUtils.getLastDay4Date(month);

        System.out.println("begin = " + begin);
        System.out.println("end = " + end);

        // 3. 调用DAO根据日期查询预约设置数据
        List<OrderSetting> list = orderSettingDAO.getOrderSettingByMonth(begin, end);

        // 4. 返回查询预约设置数据
        return list;
    }

    /**
     * 根据指定日期修改可预约人数
     * @param orderSetting
     */
    @Override
    @Transactional
    public void editNumberByOrderDate(OrderSetting orderSetting) {
        // 1. 先查询是否已经有该时间的预约设置
        OrderSetting dbOrderSetting = orderSettingDAO.findByOrderDate(orderSetting.getOrderDate());
        if (dbOrderSetting == null) {
            // 2. 没有就新增数据
            orderSettingDAO.add(orderSetting);
        } else {
            // 3. 有就更新数据
            orderSettingDAO.editNumberByOrderDate(orderSetting);
        }
    }
}
