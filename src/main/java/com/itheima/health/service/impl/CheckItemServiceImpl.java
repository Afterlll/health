package com.itheima.health.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.health.dao.CheckItemDAO;
import com.itheima.health.entity.PageResult;
import com.itheima.health.entity.QueryPageBean;
import com.itheima.health.pojo.CheckItem;
import com.itheima.health.service.CheckItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;

@Primary
@Service
public class CheckItemServiceImpl implements CheckItemService {

    // 注入DAO
    @Autowired
    private CheckItemDAO checkItemDAO;

    /**
     * 添加检查项
     * @param checkItem
     */
    @Override
    public void add(CheckItem checkItem) {
        // 调用DAO增加检查项
        checkItemDAO.add(checkItem);
    }

    /**
     * 检查项分页查询
     * @param queryPageBean
     * @return
     */
    @Override
    public PageResult findPage(QueryPageBean queryPageBean) {
        // 1. 使用分页插件设置分页参数
        // PageHelper.startPage(当前页，每页显示的数量)
        // 分页插件会计算：跳过的数量 = （当前页 - 1） * 每页显示的数量
        PageHelper.startPage(queryPageBean.getCurrentPage(), queryPageBean.getPageSize());
        // 2. 调用DAO查询
        Page<CheckItem> page = checkItemDAO.findPage(queryPageBean.getQueryString());
        // 3. 处理结果
        return new PageResult(page.getTotal(), page.getResult());
    }

    /**
     * 删除检查项
     * @param id
     * @return
     */
    @Override
    public boolean delete(int id) {
        // 1.查询当前检查项是否和检查组关联
        int count = checkItemDAO.findCountByCheckItemId(id);
        // 2.如果当前检查项在检查组中不能删除, 返回false
        if (count > 0) {
            return false;
        }
        // 3.检查项没有在检查组中,删除检查项
        return checkItemDAO.delete(id);
    }

    /**
     * 回显数据
     * @param id
     * @return
     */
    @Override
    public CheckItem findById(int id) {
        return checkItemDAO.findById(id);
    }

    /**
     * 编辑检查项
     * @param checkItem
     */
    @Override
    public void edit(CheckItem checkItem) {
        checkItemDAO.edit(checkItem);
    }

    /**
     * 查询所有检查项
     * @return 所有检查项数据
     */
    @Override
    public List<CheckItem> findAll() {
        return checkItemDAO.findAll();
    }
}
