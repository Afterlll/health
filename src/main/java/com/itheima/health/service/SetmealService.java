package com.itheima.health.service;

import com.github.pagehelper.Page;
import com.itheima.health.entity.PageResult;
import com.itheima.health.entity.QueryPageBean;
import com.itheima.health.pojo.Setmeal;

import java.util.List;

public interface SetmealService {
    void add(Setmeal setmeal, Integer[] checkgroupIds);

    PageResult findPage(QueryPageBean queryPageBean);

    Setmeal findById(Integer id);

    List<Integer> findCheckGroupIdsBySetmealId(Integer setmealId);

    boolean edit(Setmeal setmeal, Integer[] checkgroupIds);

    boolean delete(Integer id);
}
