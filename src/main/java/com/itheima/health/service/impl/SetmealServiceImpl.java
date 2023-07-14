package com.itheima.health.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.health.dao.SetmealDAO;
import com.itheima.health.entity.PageResult;
import com.itheima.health.entity.QueryPageBean;
import com.itheima.health.pojo.Setmeal;
import com.itheima.health.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
public class SetmealServiceImpl implements SetmealService {
    @Autowired
    private SetmealDAO setmealDAO;

    /**
     * 获取套餐和套餐的预约数量
     * @return 套餐和套餐的预约数量
     */
    @Override
    public List<Map> findSetmealCount() {
        List<Map> list = setmealDAO.findSetmealCount();
        return list;
    }

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
     * 分页显示套餐
     * @return
     */
    @Override
    public PageResult findPage(QueryPageBean queryPageBean) {
        // 1. 使用分页插件开始分页,即让插件为我们即将执行的select语句拼接上”LIMIE“关键字
        PageHelper.startPage(queryPageBean.getCurrentPage(), queryPageBean.getPageSize());
        // 2. 调用DAO实现查询功能（可能带条件）
        Page<Setmeal> page = setmealDAO.findPage(queryPageBean.getQueryString());
        // 3. 返回结果
        return new PageResult(page.getTotal(), page.getResult());
    }

    /**
     * 根据id查询套餐信息
     * @param id
     * @return
     */
    @Override
    public Setmeal findById(Integer id) {
        return setmealDAO.findById(id);
    }

    /**
     * 根据套餐id去中间表查询出对应的检查组id
     * @param setmealId
     * @return
     */
    @Override
    public List<Integer> findCheckGroupIdsBySetmealId(Integer setmealId) {
        return setmealDAO.findCheckGroupIdsBySetmealId(setmealId);
    }

    /**
     * 编辑套餐信信息
     * @param setmeal
     * @param checkgroupIds
     * @return
     */
    @Override
    @Transactional // 增加事务，保障编辑逻辑的完整性
    public boolean edit(Setmeal setmeal, Integer[] checkgroupIds) {
        // 1. 更改套餐信息
        setmealDAO.edit(setmeal);
        // 2. 删除原来的关联信息
        setmealDAO.deleteAssociation(setmeal.getId());
        // 3. 增加新的关联信息
        setSetmealAndCheckgroup(setmeal.getId(), checkgroupIds);
        return true;
    }

    /**
     * 删除套餐信息
     * @param id
     * @return
     */
    @Override
    @Transactional // 添加事务
    public boolean delete(Integer id) {
        // 1. 先删除中间表
        setmealDAO.deleteAssociation(id);
        // 2. 删除套餐
        setmealDAO.delete(id);
        return true;
    }

    /**
     * 查询套餐列表信息
     * @return
     */
    @Override
    public List<Setmeal> findAll() {
        return setmealDAO.findAll();
    }

    /**
     * 根据id查询套餐信息
     * @param id 套餐id
     * @return 一条套餐信息，包含所有的检查组和检查项
     */
    @Override
    public Setmeal findFullSetmealById(Integer id) {
        // 1.调用DAO获取一条套餐信息，包含所有的检查组和检查项
        Setmeal setmeal = setmealDAO.findFullSetmealById(id);

        // 2.返回一条套餐信息，包含所有的检查组和检查项
        return setmeal;
    }

    /**
     * 增加中间表数据
     * @param setmealId
     * @param checkgroupIds
     */
    private void setSetmealAndCheckgroup(Integer setmealId, Integer[] checkgroupIds) {
        if (checkgroupIds != null && checkgroupIds.length > 0) {
            for (Integer checkgroupId : checkgroupIds) {
                setmealDAO.setSetmealAndCheckGroupId(setmealId, checkgroupId);
            }
        }
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
