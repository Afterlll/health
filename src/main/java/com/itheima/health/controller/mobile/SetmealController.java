package com.itheima.health.controller.mobile;

import com.itheima.health.constant.MessageConstant;
import com.itheima.health.entity.Result;
import com.itheima.health.pojo.Setmeal;
import com.itheima.health.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 移动端的套餐列表页面动态展示控制器
 */
@RestController("mobileSetmealController") // 控制器必须要起一个跟之前后台的SetmealController不一样的名字，不让容器中的bean就冲突了
@RequestMapping("/setmeal")
public class SetmealController {
    @Autowired
    private SetmealService setmealService;

    /**
     * 获取所有套餐信息
     * @return 统一响应结果，包含所有套餐信息
     */
    @RequestMapping("/getSetmeal.do")
    public Result getSetmeal() {
        // 1. 调用业务层
        List<Setmeal> setmeals = setmealService.findAll();
        // 2. 返回统一结果
        return new Result(true, MessageConstant.QUERY_SETMEALLIST_SUCCESS, setmeals);
    }

    /**
     * 根据id查询套餐信息
     * @param id 套餐id
     * @return 统一响应结果，包含一条套餐信息，包含所有的检查组和检查项
     */
    @RequestMapping("/findFullSetmealById.do")
    public Result findFullSetmealById(Integer id) {
        // 1.调用业务层根据套餐获取一条套餐信息，包含所有的检查组和检查项
        Setmeal setmeal = setmealService.findFullSetmealById(id);

        // 2.响应统一结果Result，包含一条套餐信息
        return new Result(true, MessageConstant.QUERY_SETMEAL_SUCCESS, setmeal);
    }
}
