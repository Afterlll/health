package com.itheima.health.controller.backend;

import com.itheima.health.constant.MessageConstant;
import com.itheima.health.entity.PageResult;
import com.itheima.health.entity.QueryPageBean;
import com.itheima.health.entity.Result;
import com.itheima.health.pojo.CheckItem;
import com.itheima.health.service.CheckItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 检查项控制器
 */
@RestController
@RequestMapping("/checkitem")
public class CheckItemController {
    // 注入service
    @Autowired
    private CheckItemService checkItemService;

    /**
     * 新增检查项
     * @param checkItem
     * @return
     */
    @PostMapping("/add.do")
    public Result add(@RequestBody CheckItem checkItem) {
        // 1. 调用业务层增加检查项
        checkItemService.add(checkItem);

        // 2. 统一响应结果Result
        return new Result(true, MessageConstant.ADD_CHECKGROUP_SUCCESS);
    }

    /**
     * 检查项分页显示
     * @param queryPageBean
     * @return
     */
    @PostMapping("/findPage.do")
        public PageResult findPage(@RequestBody QueryPageBean queryPageBean) {
        // 调用业务层分页查询
        PageResult pageResult = checkItemService.findPage(queryPageBean);
        // findPage配置的SQL语句：select * from t_checkitem
        // 分页查询会做以下事情
        // 分页插件会修改SQL语句，第一次查询总数量：SELECT count(0) FROM t_checkitem
        // 分页插件会修改SQL语句，第二次是真正拼接好的分页查询语句：select * from t_checkitem LIMIT ?, ?
        // 返回分页同意结果
        return pageResult;
    }

    /**
     * 根据id删除检查项
     * @param id
     * @return
     */
    @GetMapping("/delete.do")
    public Result delete(int id) {
        // 1. 调用业务层
        boolean isDelete = checkItemService.delete(id);
        // 2. 返回结果
        return new Result(isDelete, isDelete ? MessageConstant.DELETE_CHECKITEM_SUCCESS : MessageConstant.DELETE_CHECKITEM_FAIL);
    }

    /**
     * 回显编辑检查项时的数据
     * @param id
     * @return
     */
    @GetMapping("/findById.do")
    public Result findById(int id) {
        // 1. 调用业务层根据id查询检查项
        CheckItem checkItem = checkItemService.findById(id);
        // 2. 响应统一结果Result,包含检查项数据
        return new Result(true, MessageConstant.QUERY_CHECKITEM_SUCCESS, checkItem);
    }

    /**
     * 编辑检查项
     * @param checkItem
     * @return
     */
    @PostMapping("/edit.do")
    public Result edit(@RequestBody CheckItem checkItem) {
        // 1.调用业务层编辑检查项
        checkItemService.edit(checkItem);
        // 2.响应统一结果Result
        return new Result(true, MessageConstant.EDIT_CHECKITEM_SUCCESS);
    }

    /**
     * 查询所有检查项
     * @return 所有检查项数据
     */
    @RequestMapping("/findAll.do")
    public Result findAll() {
        // 1.调用业务层查询所有检查项
        List<CheckItem> checkItems = checkItemService.findAll();
        // 2.响应Result
        return new Result(true, MessageConstant.QUERY_CHECKITEM_SUCCESS, checkItems);
    }
}
