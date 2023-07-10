package com.itheima.health.dao;

import com.itheima.health.pojo.Setmeal;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SetmealDAO {
    /**
     * 新增套餐
     * @param setmeal 套餐数据
     */
    @Insert("insert into t_setmeal  values (null, #{name}, #{code}, #{helpCode}, #{sex}, #{age}, #{price}, #{remark}, #{attention}, #{img});")
    void add(Setmeal setmeal);

    /**
     * 在中间表中添加套餐和检查组
     * @param setmealId
     * @param checkgroupId
     */
    @Insert("insert into t_setmeal_checkgroup values (#{setmeal_id}, #{checkgroup_id});")
    void setSetmealAndCheckGroupId(Integer setmealId, Integer checkgroupId);
}
