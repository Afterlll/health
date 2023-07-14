package com.itheima.health.controller.backend;

import com.itheima.health.constant.MessageConstant;
import com.itheima.health.entity.PageResult;
import com.itheima.health.entity.QueryPageBean;
import com.itheima.health.entity.Result;
import com.itheima.health.pojo.Setmeal;
import com.itheima.health.service.SetmealService;
import com.itheima.health.utils.QiniuUtils;
import com.itheima.health.utils.UUIDUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/setmeal")
public class SetmealController {
    @Autowired
    private SetmealService setmealService;

    /**
     * 上传文件
     * @param multipartFile
     * @return
     */
    @RequestMapping("/upload.do")
    public Result upload(@RequestPart("imgFile") MultipartFile multipartFile) {
        // 获取文件的原始名字
        String originalFilename = multipartFile.getOriginalFilename();
        // 为了防止文件名重复,随机生成文件名: 8d1f624e0314405690ff87b09183a56c-a.jpg
        String fileName = UUIDUtils.uuid() + "-" + originalFilename;
        // 把文件上传到七牛云
        try {
            QiniuUtils.upload2Qiniu(multipartFile.getBytes(), fileName);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        // 返回结果，附上文件名字
        return new Result(true, MessageConstant.UPLOAD_SUCCESS, fileName);
    }

    /**
     * 新增套餐
     * @param setmeal 套餐数据
     * @param checkgroupIds 套餐包含的检查组
     * @return 统一响应结果
     */
    @PostMapping("/add.do")
    public Result add(@RequestBody Setmeal setmeal, Integer[] checkgroupIds) {
        // 1.调用业务层新增套餐
        setmealService.add(setmeal, checkgroupIds);

        // 2.响应统一结果Result
        return new Result(true, MessageConstant.ADD_SETMEAL_SUCCESS);
    }

    /**
     * 套餐分页显示
     * @return
     */
    @PostMapping("/findPage.do")
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean) {
        // 1. 调用业务层
        PageResult pageResult = setmealService.findPage(queryPageBean);
        // 2. 返回带数据的统一结果
        return pageResult;
    }

    /**
     * 根据id查询出对应的套餐信息
     * @param id
     * @return
     */
    @GetMapping("/findById.do")
    public Result findById(Integer id) {
        // 1. 调用业务层进行查询
        Setmeal setmeal = setmealService.findById(id);
        // 2. 返回
        return new Result(true, MessageConstant.QUERY_SETMEAL_SUCCESS, setmeal);
    }

    /**
     * 根据套餐id去中间表查询出对应的检查组id
     * @param setmealId
     * @return
     */
    @GetMapping("/findCheckGroupIdsBySetmealId.do")
    public Result findCheckGroupIdsBySetmealId(Integer setmealId) {
        // 1. 调用业务层
        List<Integer> checkGroupIds = setmealService.findCheckGroupIdsBySetmealId(setmealId);
        // 2. 返回结果
        return new Result(true, MessageConstant.QUERY_SETMEAL_SUCCESS, checkGroupIds);
    }

    /**
     * 编辑套餐信息
     * @param setmeal 前端传过来的套餐信息
     * @param checkgroupIds 前端传过来的套餐绑定的检查组id
     * @return
     */
    @PostMapping("/edit.do")
    public Result edit(@RequestBody Setmeal setmeal, Integer[] checkgroupIds) {
        // 1. 调用业务层
        boolean isEdit = setmealService.edit(setmeal, checkgroupIds);
        // 2. 返回结果
        return new Result(isEdit, isEdit ? MessageConstant.EDIT_SETMEAL_SUCCESS : MessageConstant.EDIT_SETMEAL_FAIL);
    }

    /**
     * 删除套餐
     * @param id
     * @return
     */
    @GetMapping("delete.do")
    public Result delete(Integer id) {
        // 1. 调用业务层
        boolean isDelete = setmealService.delete(id);
        // 2. 返回
        return new Result(isDelete, isDelete ? MessageConstant.DELETE_SETMEAL_SUCCESS : MessageConstant.DELETE_SETMEAL_FAIL);
    }
}
