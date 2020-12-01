package com.highershine.portal.common.mapper;

import com.highershine.portal.common.entity.po.SysDictCategory;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Description: 字典类型mapper
 * @Author: xueboren
 * @Date: 2019/11/27 19:30
 */
@Repository
public interface SysDictCategoryMapper {

    /**
     * 根据id删除
     *
     * @param id
     * @return
     */
    int deleteByPrimaryKey(Long id);

    /**
     * 插入字典类型
     *
     * @param record
     * @return
     */
    int insert(SysDictCategory record);

    /**
     * 根据id查询
     *
     * @param id
     * @return
     */
    SysDictCategory selectByPrimaryKey(Long id);

    /**
     * 更新字典类型
     *
     * @param record
     * @return
     */
    int updateByPrimaryKey(SysDictCategory record);

    /**
     * 查询类别列表
     *
     * @return
     */
    List<SysDictCategory> selectSysDictCategoryList();
}
