package com.itheima.health.dao;

import com.github.pagehelper.Page;
import com.itheima.health.pojo.CheckGroup;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CheckGroupDAO {

    /**
     * 新增检查组
     * @param checkGroup 检查组信息
     */
    @Insert("insert into t_checkgroup values (null, #{code}, #{name}, #{helpCode}, #{sex}, #{remark}, #{attention});")
    /**
     * @Options
     * 针对insert有效，当有关联表操作时，可以先插入主表，如何根据主表返回的主键id去落库详情表
     * useGeneratedKeys ： 是否返回生成的主键
     * keyProperty ：传入对象的字段名
     * keyColumn ：数据库中的字段名
     */
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void add(CheckGroup checkGroup);

    /**
     * 新增检查组和检查项的关联信息
     * @param checkGroupId
     * @param checkItemId
     */
    @Insert("insert into t_checkgroup_checkitem values (#{checkGroupId}, #{checkItemId});")
    void setCheckGroupAndCheckItem(Integer checkGroupId, Integer checkItemId);

    /**
     * 分页显示所有检查组的信息
     * 此处的分页依旧采取动态SQL拼接
     * @param queryString
     * @return
     */
    Page<CheckGroup> findPage(String queryString);

    /**
     * 根据id查询检查组信息
     * @param id
     * @return
     */
    @Select("select * from t_checkgroup where id = #{id};")
    CheckGroup findById(Integer id);

    /**
     * 根据id查询检查组的信息
     * @param checkGroupId
     * @return
     */
    @Select("select checkitem_id from t_checkgroup_checkitem where checkgroup_id = #{checkGroupId};")
    List<Integer> indCheckItemIdsByCheckGroupId(Integer checkGroupId);

    /**
     * 更新检查组信息
     * 这里使用动态SQL
     * @param checkGroup
     */
    void edit(CheckGroup checkGroup);

    /**
     * 删除检查组和检查项的关联信息
     * @param id
     */
    @Delete("delete from t_checkgroup_checkitem where checkgroup_id = #{checkgroup_id};")
    void deleteAssociation(Integer id);

    /**
     * 删除一条检查组
     * @param id
     */
    @Delete("delete from t_checkgroup where id = #{id};")
    void delete(Integer id);

    /**
     * 查询所有的检查组
     * @return
     */
    @Select("select * from t_checkgroup;")
    List<CheckGroup> findAll();
}
