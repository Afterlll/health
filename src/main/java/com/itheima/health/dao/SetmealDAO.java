package com.itheima.health.dao;

import com.github.pagehelper.Page;
import com.itheima.health.pojo.CheckGroup;
import com.itheima.health.pojo.CheckItem;
import com.itheima.health.pojo.Setmeal;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Map;

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

    /**
     * 查询所有套餐信息
     * @return
     */
    @Select("select  * from t_setmeal;")
    List<Setmeal> findAll();

    /**
     * 根据id查询套餐信息
     * @param id 套餐id
     * @return 一条套餐信息，包含所有的检查组和检查项
     */
    @Select("select * from t_setmeal where id = #{id};")
    /**
     * 1. @Results 注解用于配置结果映射规则。在这个例子中，@Results 注解用于定义一个或多个结果映射规则，并将其应用于查询结果中的属性。
     * 2. @Result 注解是 @Results 注解的子注解，用于配置单个属性的结果映射规则。在这个例子中，它被用来配置 checkGroups 属性的结果映射规则。
     * 3. property = "checkGroups" 指定了要映射的 Java 对象的属性名为 "checkGroups"。
     * 4. column = "id" 指定了数据库中用于映射的列名为 "id"。
     * 5. many = @Many(select = "findCheckGroupBySetmealId") 表示一个一对多的关系，即一个套餐可以对应多个检查组。
     *      @Many 注解表示查询多个结果，select = "findCheckGroupBySetmealId" 指定了用于查询的方法名为 "findCheckGroupBySetmealId"。
     *      这个方法应该存在于相同的 DAO 接口中，并返回一个检查组的列表。
     */
    @Results(@Result(property = "checkGroups", column = "id", many = @Many(select = "findCheckGroupBySetmealId")))
    Setmeal findFullSetmealById(Integer id);

    /**
     * 根据套餐id,查询该套餐下的所有检查组
     * @param setmealId 套餐id
     * @return
     */
    @Select("select * from t_checkgroup where id in (select checkgroup_id from t_setmeal_checkgroup where setmeal_id = #{setmealId});")
    @Results(@Result(property = "checkItems", column = "id", many = @Many(select = "findCheckItemByGroupId")))
    List<CheckGroup> findCheckGroupBySetmealId(Integer setmealId);

    /**
     * 根据检查组id查询出对应的所有检查项
     * @param checkGroupId 检查组id
     * @return
     */
    @Select("select * from t_checkitem where id in (select checkitem_id from t_checkgroup_checkitem where checkgroup_id = #{checkGroupId});")
    List<CheckItem> findCheckItemByGroupId(Integer checkGroupId);

    /**
     * 获取套餐和套餐的预约数量
     * @return 套餐和套餐的预约数量
     */
    @Select("select t_setmeal.name, count(*) value from t_order inner join t_setmeal on t_order.setmeal_id = t_setmeal.id group by t_setmeal.name;")
    List<Map> findSetmealCount();

}
