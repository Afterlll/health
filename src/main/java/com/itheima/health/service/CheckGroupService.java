package com.itheima.health.service;

import com.itheima.health.entity.PageResult;
import com.itheima.health.entity.QueryPageBean;
import com.itheima.health.pojo.CheckGroup;

import java.util.List;

public interface CheckGroupService {

    void add(CheckGroup checkGroup, Integer[] checkItemIds);

    PageResult findPage(QueryPageBean queryPageBean);

    CheckGroup findById(Integer id);

    /**
     * 根据检查组id查询对应的所有检查项id
     * @param checkGroupId 检查组id
     * @return 检查组合的所有检查项id
     */
    List<Integer> indCheckItemIdsByCheckGroupId(Integer checkGroupId);

    void edit(CheckGroup checkGroup, Integer[] checkItemIds);

    boolean delete(Integer id);

    List<CheckGroup> findAll();
}
