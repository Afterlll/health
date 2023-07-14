package com.itheima.health.dao;

import com.itheima.health.pojo.Member;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

import java.util.Date;

@Mapper
public interface MemberDAO {
    /**
     * 根据手机号检查当前用户是否为会员
     * @param telephone 手机号
     * @return 会员
     */
    @Select("select * from t_member where phoneNumber = #{telephone};")
    Member findByTelephone(String telephone);

    /**
     * 添加会员
     * @param member 会员数据
     */
    @Insert("insert into t_member values (null, #{fileNumber}, #{name}, #{sex}, #{idCard}, #{phoneNumber}, #{regTime}, #{password}, #{email}, #{birthday}, #{remark});")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void add(Member member);

    /**
     * 根据日期统计会员数，统计指定日期之前的会员数
     * @param date
     * @return
     */
    @Select("select count(*) from t_member where regTime <= #{date};")
    int findMemberCountByMonth(Date date);

    /**
     * 今日新增会员数
     * @param today
     * @return
     */
    @Select("select count(*) from t_member where regTime = date(#{today});")
    Integer findMemberCountByDate(Date today);

    /**
     * 总会员数
     * @return
     */
    @Select("select count(*) from t_member;")
    Integer findMemberTotalCount();

    /**
     * 查询指定时间之后的会员数量
     * @param date
     * @return
     */
    @Select("select count(*) from t_member where regTime >= date(#{date});")
    Integer findMemberCountAfterDate(Date date);
}