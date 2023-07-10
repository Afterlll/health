package com.itheima.health.service.impl;

import com.itheima.health.dao.SetmealDAO;
import com.itheima.health.pojo.Setmeal;
import com.itheima.health.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SetmealServiceImpl implements SetmealService {
    @Autowired
    private SetmealDAO setmealDAO;

    /**
     * 新增套餐
     * @param setmeal 套餐数据
     * @param checkgroupIds 套餐包含的检查组
     */
    @Override
    public void add(Setmeal setmeal, Integer[] checkgroupIds) {
        // 1. 调用DAO层在Setmeal套餐表中添加数据
        setmealDAO.add(setmeal);
        // 2. 调用DAO在中间表中添加套餐和检查组
        setSetmealAndCheckGroup(setmeal.getId(), checkgroupIds);
    }

    /**
     * 在中间表中添加套餐和检查组
     * @param setmealId 套餐id
     * @param checkgroupIds 套餐对应的所有检查组id
     */
    private void setSetmealAndCheckGroup(Integer setmealId, Integer[] checkgroupIds) {
        if (checkgroupIds == null || checkgroupIds.length == 0) {
            System.out.println("套餐没有对应的检查组");
            return;
        }

        // 把套餐的id和对应的检查组id添加到中间表
        for (Integer checkgroupId : checkgroupIds) {
            setmealDAO.setSetmealAndCheckGroupId(setmealId, checkgroupId);
        }
    }
}
