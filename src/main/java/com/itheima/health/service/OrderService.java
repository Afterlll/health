package com.itheima.health.service;

import com.itheima.health.entity.Result;

import java.util.Map;

public interface OrderService {
    /**
     * 体检预约服务完成预约
     * @param map 前端提交的预约参数
     * @return 统一响应结果，包含预约结果
     */
    Result order(Map<String, String> map);

    Map findById(Integer id);
}
