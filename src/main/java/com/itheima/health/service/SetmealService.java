package com.itheima.health.service;

import com.itheima.health.entity.PageResult;
import com.itheima.health.entity.QueryPageBean;
import com.itheima.health.pojo.Setmeal;

import java.util.List;
import java.util.Map;

public interface SetmealService {
    void add(Setmeal setmeal, Integer[] checkgroupIds);

    PageResult findPage(QueryPageBean queryPageBean);

    Setmeal findById(Integer id);

    List<Integer> findCheckGroupIdsBySetmealId(Integer setmealId);

    boolean edit(Setmeal setmeal, Integer[] checkgroupIds);

    boolean delete(Integer id);

    List<Setmeal> findAll();

    Setmeal findFullSetmealById(Integer id);

    /**
     * 获取套餐和套餐的预约数量
     * @return 套餐和套餐的预约数量
     */
    List<Map> findSetmealCount();
}
