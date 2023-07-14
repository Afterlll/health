package com.itheima.health.service;

import java.util.Map;

public interface ReportService {
    /**
     * 获取运营统计数据
     * @return 统一响应结果，包含运营统计数据
     * Map数据格式：
     *      reportDate              -> date     日期
     *      todayNewMember          -> number   新增会员数
     *      totalMember             -> number   总会员数
     *      thisWeekNewMember       -> number   本周新增会员数
     *      thisMonthNewMember      -> number   本月新增会员数
     *      todayOrderNumber        -> number   今日预约数
     *      todayVisitsNumber       -> number   今日到诊数
     *      thisWeekOrderNumber     -> number   本周预约数
     *      thisWeekVisitsNumber    -> number   本周到诊数
     *      thisMonthOrderNumber    -> number   本月预约数
     *      thisMonthVisitsNumber   -> number   本月到诊数
     *      hotSetmeal -> List<Map>             热门套餐    {name:'阳光爸妈升级肿瘤12项筛查（男女单人）体检套餐',setmeal_count:200,proportion:0.222}
     */
    Map<String, Object> getBusinessReport() throws Exception;

}
