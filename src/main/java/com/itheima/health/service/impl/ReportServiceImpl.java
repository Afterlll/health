package com.itheima.health.service.impl;

import com.itheima.health.dao.MemberDAO;
import com.itheima.health.dao.OrderDAO;
import com.itheima.health.service.ReportService;
import com.itheima.health.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ReportServiceImpl implements ReportService {
    @Autowired
    private MemberDAO memberDao;

    @Autowired
    private OrderDAO orderDao;

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
    @Override
    public Map<String, Object> getBusinessReport() throws Exception {
        // 1.获得当前日期
        Date today = DateUtils.getToday();

        // 2.获得本周一的日期
        Date thisWeekMonday = DateUtils.getThisWeekMonday();

        // 3.获得本月第一天的日期
        Date firstDay4ThisMonth = DateUtils.getFirstDay4ThisMonth();

        // 4.今日新增会员数
        Integer todayNewMember = memberDao.findMemberCountByDate(today);

        // 5.总会员数
        Integer totalMember = memberDao.findMemberTotalCount();

        // 6.本周新增会员数
        Integer thisWeekNewMember = memberDao.findMemberCountAfterDate(thisWeekMonday);

        // 7.本月新增会员数
        Integer thisMonthNewMember = memberDao.findMemberCountAfterDate(firstDay4ThisMonth);

        // 8.今日预约数
        Integer todayOrderNumber = orderDao.findOrderCountByDate(today);

        // 9.今日到诊数
        Integer todayVisitsNumber = orderDao.findVisitsCountByDate(today);

        // 10.本周预约数
        Integer thisWeekOrderNumber = orderDao.findOrderCountAfterDate(thisWeekMonday);

        // 11.本周到诊数
        Integer thisWeekVisitsNumber = orderDao.findVisitsCountAfterDate(thisWeekMonday);

        // 12.本月预约数
        Integer thisMonthOrderNumber = orderDao.findOrderCountAfterDate(firstDay4ThisMonth);

        // 13.本月到诊数
        Integer thisMonthVisitsNumber = orderDao.findVisitsCountAfterDate(firstDay4ThisMonth);

        // 14.热门套餐（取前4） {name:'阳光爸妈升级肿瘤12项筛查（男女单人）体检套餐',setmeal_count:200,proportion:0.222}
        List<Map> hotSetmeal = orderDao.findHotSermeal();

        Map<String, Object> result = new HashMap<>();
        result.put("reportDate", DateUtils.getTodayString());
        result.put("todayNewMember", todayNewMember);
        result.put("totalMember", totalMember);
        result.put("thisWeekNewMember", thisWeekNewMember);
        result.put("thisMonthNewMember", thisMonthNewMember);
        result.put("todayOrderNumber", todayOrderNumber);
        result.put("todayVisitsNumber", todayVisitsNumber);
        result.put("thisWeekOrderNumber", thisWeekOrderNumber);
        result.put("thisWeekVisitsNumber", thisWeekVisitsNumber);
        result.put("thisMonthOrderNumber", thisMonthOrderNumber);
        result.put("thisMonthVisitsNumber", thisMonthVisitsNumber);
        result.put("hotSetmeal", hotSetmeal);

        return result;
    }
}