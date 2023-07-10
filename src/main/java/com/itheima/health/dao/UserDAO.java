package com.itheima.health.dao;

import com.itheima.health.pojo.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper // 动态代理：MyBatis自动生成DAO层的实现类，并且将实现类对象放到容器中
public interface UserDAO {
    /**
     * 根据用户名查询用户
     * @param username
     * @return
     */
    @Select("select * from t_user where username = #{username};")
    User findByUsername(String username);

    /**
     * 根据手机号查询用户
     * @param telephone
     * @return
     */
    @Select("select * from t_user where telephone = #{telephone};")
    User findByTelephone(String telephone);

    /**
     * 插入用户，字段有用户名、密码、手机号
     * @param registerUser
     */
    @Insert("insert into t_user(username, telephone, password) values (#{username}, #{telephone}, #{password});")
    void register(User registerUser);
}
