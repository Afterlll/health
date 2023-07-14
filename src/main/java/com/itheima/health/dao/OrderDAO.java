package com.itheima.health.dao;

import com.itheima.health.pojo.Order;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Mapper
public interface OrderDAO { // 440514200302093016
    /**
     * 根据member_id, orderDate, setmeal_id查询预约信息
     * @param order 查询条件member_id, orderDate, setmeal_id
     * @return 预约信息对象
     */
    @Select("select * from t_order where member_id = #{memberId} and orderDate = #{orderDate} and setmeal_id = #{setmealId};")
    List<Order> findByCondition(Order order);

    /**
     * 保存预约信息
     * @param order 预约信息
     */
    @Insert("insert into t_order values (null, #{memberId}, #{orderDate}, #{orderType}, #{orderStatus}, #{setmealId});")
    @Options(useGeneratedKeys = true, keyColumn = "id", keyProperty = "id")
    void add(Order order);

    /**
     * 根据预约id查询预约信息，包括套餐信息和会员信息
     * @param id 预约id
     * @return 预约信息，使用Map保存前端需要的数据， member, setmeal, dorderDate, orderType
     */
    @Select("select m.name `member`, s.name setmeal, o.orderDate orderDate, o.orderType orderType from t_order o " +
            "inner join t_member m on o.member_id = m.id inner join t_setmeal s on o.setmeal_id = s.id where o.id = #{id};")
    Map findFullOrderById(Integer id);

    /**
     * 今日预约数
     * @param today
     * @return
     */
    @Select("select count(*) from t_order where orderDate = date(#{today});")
    Integer findOrderCountByDate(Date today);

    /**
     * 今日到诊数
     * @param today
     * @return
     */
    @Select("select count(*) from t_order where orderDate = date(#{today}) and orderStatus = '已到诊';")
    Integer findVisitsCountByDate(Date today);

    /**
     * 指定日期后的预约数
     * @param date
     * @return
     */
    @Select("select count(*) from t_order where orderDate >= date(#{date});")
    Integer findOrderCountAfterDate(Date date);

    /**
     * 指定日期后的到诊数
     * @param date
     * @return
     */
    @Select("select count(*) from t_order where orderDate >= date(#{date}) and orderStatus = '已到诊';")
    Integer findVisitsCountAfterDate(Date date);

    /**
     * 热门套餐
     * @return  {name:'阳光爸妈升级肿瘤12项筛查（男女单人）体检套餐',setmeal_count:200,proportion:0.222}
     */
    @Select("select t_setmeal.name, count(*) setmeal_count, count(*) / (select count(*) from t_order) proportion from t_setmeal inner join t_order on t_setmeal.id = t_order.setmeal_id group by t_setmeal.name limit 4;")
    List<Map> findHotSermeal();
}