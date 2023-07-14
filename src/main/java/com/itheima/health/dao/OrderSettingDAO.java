package com.itheima.health.dao;

import com.itheima.health.pojo.OrderSetting;
import org.apache.ibatis.annotations.*;

import java.util.Date;
import java.util.List;

@Mapper
public interface OrderSettingDAO {
    /**
     * 添加预约数量
     * 将上传文件中的内容读取到数据库中
     * @param orderSetting
     */
    @Insert("insert into t_ordersetting values (null, #{orderDate}, #{number}, #{reservations});")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void add(OrderSetting orderSetting);

    /**
     * 查询此日期是否已经有设置可预约数量
     * @param orderDate
     * @return
     */
    @Select("select * from t_ordersetting where orderDate = #{orderDate};")
    OrderSetting findByOrderDate(Date orderDate);

    /**
     * 更新预约信息
     * @param orderSetting
     */
    @Update("update t_ordersetting set number = #{number} where orderDate = #{orderDate};")
    void editNumberByOrderDate(OrderSetting orderSetting);

    /**
     * 根据日期查询预约设置数据
     * @param begin 当月开始日期 2023-6-1
     * @param end 当月结束日期 2023-6-31
     * @return 当月的预约设置数据
     */
    @Select("select * from t_ordersetting where orderDate between #{begin} and #{end};")
    List<OrderSetting> getOrderSettingByMonth(Date begin, Date end);

    /**
     * 根据日期更新预约人数
     * @param orderSetting
     */
    @Update("update t_ordersetting set reservations = #{reservations} where orderDate = #{orderDate};")
    void editReservationsByOrderDate(OrderSetting orderSetting);
}
