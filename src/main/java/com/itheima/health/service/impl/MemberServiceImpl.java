package com.itheima.health.service.impl;

import com.itheima.health.dao.MemberDAO;
import com.itheima.health.pojo.Member;
import com.itheima.health.service.MemberService;
import com.itheima.health.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class MemberServiceImpl implements MemberService {
    @Autowired
    private MemberDAO memberDao;

    @Override
    public Member findByTelephone(String telephone) {
        return null;
    }

    @Override
    public void add(Member member) {

    }

    /**
     * 根据日期统计会员数，统计指定日期之前的会员数
     * @param month 月份
     * @return 指定月份的会员
     */
    @Override
    public List<Integer> findMemberCountByMonth(List<String> month) throws ParseException {
        // 1.定义List集合保存每个月份的会员数量
        List<Integer> data = new ArrayList<>();

        // 2.遍历获取每个月份: ['2022.07', '2022.08', '2022.09', '2022.10']
        for (String m : month) {
            // 3.把月份字符串转成Date并且带上每月最后一天 2022.07变成 2022-07-31
            Date date = DateUtils.getLastDay4Date(m, "yyyy.MM");
            // 4.调用DAO根据日期统计会员数，统计指定日期之前的会员数
            int count = memberDao.findMemberCountByMonth(date);
            // 5.把会员数量添加到List集合中
            data.add(count);
        }

        // 6.返回数据
        return data;
    }
}