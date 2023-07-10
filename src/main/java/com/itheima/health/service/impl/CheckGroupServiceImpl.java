package com.itheima.health.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.health.dao.CheckGroupDAO;
import com.itheima.health.entity.PageResult;
import com.itheima.health.entity.QueryPageBean;
import com.itheima.health.pojo.CheckGroup;
import com.itheima.health.service.CheckGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CheckGroupServiceImpl implements CheckGroupService {
    @Autowired
    private CheckGroupDAO checkGroupDAO;

    /**
     * 新增检查组
     * @param checkGroup 检查组信息
     * @param checkItemIds 检查组的检查项
     */
    @Override
    public void add(CheckGroup checkGroup, Integer[] checkItemIds) {
        // 添加检查组
        checkGroupDAO.add(checkGroup);

        // 在中间表中指定检查组中包含的检查项
        setCheckGroupAndCheckItem(checkGroup.getId(), checkItemIds);
    }

    /**
     * 检查组分页查询
     * @param queryPageBean 前端提交过来的分页查询参数
     * @return 分页查询结果
     */
    @Override
    public PageResult findPage(QueryPageBean queryPageBean) {
        // 1. 使用分页插件开始分页
        PageHelper.startPage(queryPageBean.getCurrentPage(), queryPageBean.getPageSize());
        // 2.调用DAO分页查询
        Page<CheckGroup> page = checkGroupDAO.findPage(queryPageBean.getQueryString());
        return new PageResult(page.getTotal(), page.getResult());
    }

    /**
     * 根据id返回该组的信息
     * @param id
     * @return
     */
    @Override
    public CheckGroup findById(Integer id) {
        return checkGroupDAO.findById(id);
    }

    /**
     * 根据检查组id查询对应的所有检查项id
     * @param checkGroupId 检查组id
     * @return 检查组合的所有检查项id
     */
    @Override
    public List<Integer> indCheckItemIdsByCheckGroupId(Integer checkGroupId) {
        return checkGroupDAO.indCheckItemIdsByCheckGroupId(checkGroupId);
    }

    /**
     * 编辑检查组
     * @param checkGroup
     * @param checkItemIds
     */
    @Override
    @Transactional // 这里需要事务，这几个事件必须全部一直完成
    public void edit(CheckGroup checkGroup, Integer[] checkItemIds) {
        // 1. 调用CheckGroupDAO先更改新的检查组信息
        checkGroupDAO.edit(checkGroup);
        // 2. 删除原来的检查组和检查项的关联信息
        checkGroupDAO.deleteAssociation(checkGroup.getId());
        // 3， 增加新的检查组和检查项的关联信息
        setCheckGroupAndCheckItem(checkGroup.getId(), checkItemIds);
    }

    /**
     * 删除检查组
     * @param id
     * @return
     */
    @Override
    @Transactional
    public boolean delete(Integer id) {
        // 1. 先删除关联表中的信息
        checkGroupDAO.deleteAssociation(id);
        // 2. 删除检查组表中的该组的信息
        checkGroupDAO.delete(id);
        return true;
    }

    /**
     * 查询所有的检查组
     * @return
     */
    @Override
    public List<CheckGroup> findAll() {
        return checkGroupDAO.findAll();
    }

    /**
     * 在中间表插入检查组id和检查项的id,形成关联
     * @param checkGroupId 检查组id
     * @param checkItemIds 检查组对应的所有检查项
     */
    public void setCheckGroupAndCheckItem(Integer checkGroupId, Integer[] checkItemIds) {
        // 当检查组id为空时，直接 return
        if (checkItemIds == null || checkItemIds.length == 0) {
            System.out.println("用户没有选择检查项");
            return ;
        }

        // 遍历得到每个检查项id
        for (Integer checkItemId : checkItemIds) {
            // 调用DAO往数据新增一个检查组id和一个检查项id
            checkGroupDAO.setCheckGroupAndCheckItem(checkGroupId, checkItemId);
        }
    }
}
