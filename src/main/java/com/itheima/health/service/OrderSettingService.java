package com.itheima.health.service;

import com.itheima.health.pojo.OrderSetting;

import java.util.Date;
import java.util.List;

public interface OrderSettingService {
    /**
     * 将上传文件中的内容读取到数据库中
     * @param orderSettingList
     */
    void add(List<OrderSetting> orderSettingList);

    /**
     * 根据日期查询预约设置数据(获取指定日期所在月份的预约设置数据)
     * @param month 月份，格式为：2023-6
     * @return 预约设置数据
     */
    List<OrderSetting> getOrderSettingByMonth(Date month);

    /**
     * 根据指定日期修改可预约人数
     * @param orderSetting
     */
    void editNumberByOrderDate(OrderSetting orderSetting);
}
