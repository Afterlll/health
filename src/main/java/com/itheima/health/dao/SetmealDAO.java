package com.itheima.health.dao;

import com.github.pagehelper.Page;
import com.itheima.health.pojo.Setmeal;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface SetmealDAO {
    /**
     * 新增套餐
     * @param setmeal 套餐数据
     */
    @Insert("insert into t_setmeal  values (null, #{name}, #{code}, #{helpCode}, #{sex}, #{age}, #{price}, #{remark}, #{attention}, #{img});")
    // @Option 配合着insert使用，作用：插入数据时原本是获取不到id值的，当加上这个时就可以在插入后将主键id值赋给变量，防止出现空指针异常
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void add(Setmeal setmeal);

    /**
     * 在中间表中添加套餐和检查组
     * @param setmealId
     * @param checkgroupId
     */
    @Insert("insert into t_setmeal_checkgroup values (#{setmealId}, #{checkgroupId});")
    void setSetmealAndCheckGroupId(Integer setmealId, Integer checkgroupId);

    Page<Setmeal> findPage(String queryString);

    @Select("select * from t_setmeal where id = #{id};")
    Setmeal findById(Integer id);

    @Select("select checkgroup_id from t_setmeal_checkgroup where setmeal_id = #{setmealId};")
    List<Integer> findCheckGroupIdsBySetmealId(Integer setmealId);

    /**
     * 修改套餐信息
     * 使用动态SQL
     * @param setmeal
     */
    void edit(Setmeal setmeal);

    /**
     * 删除关联关系
     * @param id
     */
    @Delete("delete from t_setmeal_checkgroup where setmeal_id = #{id};")
    void deleteAssociation(Integer id);

    @Delete("delete from t_setmeal where id = #{id};")
    void delete(Integer id);
}
