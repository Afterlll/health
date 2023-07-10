package com.itheima.health.controller.backend;

import com.itheima.health.constant.MessageConstant;
import com.itheima.health.entity.PageResult;
import com.itheima.health.entity.QueryPageBean;
import com.itheima.health.entity.Result;
import com.itheima.health.pojo.CheckGroup;
import com.itheima.health.pojo.CheckItem;
import com.itheima.health.service.CheckGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/checkgroup")
public class CheckGroupController {
    @Autowired
    private CheckGroupService checkGroupService;

    /**
     * 新增检查组
     * @param checkGroup 检查组信息
     * @param checkItemIds 检查组的检查项
     * @return
     */
    @PostMapping("/add.do")
    public Result add(@RequestBody CheckGroup checkGroup, Integer[] checkItemIds) {
        // 1.调用业务层新增检查组
        checkGroupService.add(checkGroup, checkItemIds);
        // 2.响应统一结果Result
        return new Result(true, MessageConstant.ADD_CHECKGROUP_SUCCESS);
    }

    /**
     * 检查组分页查询
     * @param queryPageBean 前端提交过来的分页查询参数
     * @return 分页查询结果
     */
    @PostMapping("/findPage.do")
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean) {
        // 1. 调用业务层分页查询
        PageResult pageResult= checkGroupService.findPage(queryPageBean);
        // 2. 返回查询结果
        return pageResult;
    }

    /**
     * 根据id查询检查组, 用于回显检查组的信息
     * @param id 检查组id
     * @return 统一响应结果,包含检查组数据
     */
    @GetMapping("/findById.do")
    public Result findById(Integer id) {
        CheckGroup checkGroup = checkGroupService.findById(id);
        return new Result(true, MessageConstant.QUERY_CHECKGROUP_SUCCESS, checkGroup);
    }

    /**
     * 根据检查组合id查询对应的所有检查项id，用于回显检查组所关联的检查项信息，也就是哪些需要勾选
     * @param checkGroupId
     * @return 统一响应结果, 包含所有检查项id
     */
    @GetMapping("/findCheckItemIdsByCheckGroupId.do")
    public Result indCheckItemIdsByCheckGroupId(Integer checkGroupId) {
        // 1. 调用业务层根据检查组合id查询对应的所有检查项id
        List<Integer> checkItemIds = checkGroupService.indCheckItemIdsByCheckGroupId(checkGroupId);
        // 2.响应统一结果Result, 包含所有检查项id
        return new Result(true, MessageConstant.QUERY_CHECKITEM_SUCCESS, checkItemIds);
    }

    /**
     * 编辑检查项
     * @param checkGroup 检查组数据
     * @return 统一响应结果
     */
    @PostMapping("/edit.do")
    public Result edit(@RequestBody CheckGroup checkGroup, Integer[] checkItemIds) {
        // 1.调用业务层编辑检查组
        checkGroupService.edit(checkGroup, checkItemIds);

        // 2.响应统一结果Result
        return new Result(true, MessageConstant.EDIT_CHECKGROUP_SUCCESS);
    }

    /**
     * 删除检查组
     * @param id
     * @return
     */
    @GetMapping("/delete.do")
    public Result delete(Integer id) {
        boolean isDelete = checkGroupService.delete(id);
        return new Result(isDelete, isDelete ? MessageConstant.DELETE_CHECKGROUP_SUCCESS : MessageConstant.DELETE_CHECKGROUP_FAIL);
    }

    /**
     * 查询所有的检查组
     * @return
     */
    @RequestMapping("/findAll.do")
    public Result findAll() {
        // 1.调用业务层查询所有检查组
        List<CheckGroup> checkGroups = checkGroupService.findAll();

        // 2.响应统一结果,包含检查组数据
        return new Result(true, MessageConstant.QUERY_CHECKGROUP_SUCCESS, checkGroups);
    }
}
