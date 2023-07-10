package com.itheima.health.dao;

import com.github.pagehelper.Page;
import com.itheima.health.pojo.CheckItem;
import org.apache.ibatis.annotations.*;
import org.springframework.beans.factory.annotation.Value;

import java.util.List;

/**
 * 检查项DAO
 */
@Mapper
public interface CheckItemDAO {
    /**
     * 添加检查项
     * @param checkItem
     */
    @Insert("insert into t_checkitem values (null, #{code}, #{name}, #{sex}, #{age}, #{price}, #{type}, #{remark}, #{attention});")
    void add(CheckItem checkItem);

    /**
     * 真正的分页SQL是 select * from t_checkitem limit 20, 10
     * 但是我们只需要写 select * from t_checkitem
     * 分页插件会帮我们自动拼接SQL语句
     * 一定要注意 ！！！！ 因此注意不能加上”;“
     *
     * @param queryString
     * @return
     */
//    @Select("select * from t_checkitem where code like concat('%', #{queryString},'%') or name like concat('%', #{queryString},'%')")
    // 当要使用上查询条件时，可以使用动态SQL
    Page<CheckItem> findPage(@Value("queryString") String queryString);

    @Delete("delete from t_checkitem where id = #{id};")
    boolean delete(int id);

    @Select("select count(*) from t_checkgroup_checkitem where checkitem_id = #{id};")
    int findCountByCheckItemId(int id);

    @Select("select * from t_checkitem where id = #{id};")
    CheckItem findById(int id);

    void edit(CheckItem checkItem);

    /**
     * 查询所有检查项
     * @return 所有检查项数据
     */
    @Select("select * from t_checkitem;")
    List<CheckItem> findAll();
}
