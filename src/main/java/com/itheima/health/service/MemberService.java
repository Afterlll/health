package com.itheima.health.service;

import com.itheima.health.pojo.Member;

import java.text.ParseException;
import java.util.List;

public interface MemberService {
    /**
     * 通过手机号查询会员
     * @param telephone 手机号
     * @return 会员对象
     */
    Member findByTelephone(String telephone);

    /**
     * 添加会员
     * @param member 会员对象
     */
    void add(Member member);

    /**
     * 获取指定月份的会员数据
     * @param month 月份
     * @return 指定月份的会员
     */
    List<Integer> findMemberCountByMonth(List<String> month) throws ParseException;
}
