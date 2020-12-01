package com.highershine.portal.common.mapper;

import com.highershine.portal.common.entity.po.SysDict;
import com.highershine.portal.common.entity.vo.SysDictVo;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Description: 字典mapper
 * @Author: xueboren
 * @Date: 2019/11/27 19:30
 */
@Repository
public interface SysDictMapper {

    /**
     * 根据id删除
     *
     * @param id
     * @return
     */
    void deleteByPrimaryKey(Long id);

    /**
     * 插入字典详情
     *
     * @param sysDict
     * @return
     */
    long insertSysDict(SysDict sysDict);

    /**
     * 根据id查询
     *
     * @param id
     * @return
     */
    SysDict selectByPrimaryKey(Long id);

    /**
     * 根据类别查询
     *
     * @param category
     * @return
     */
    List<SysDict> selectSysDictListByCategory(String category);

    /**
     * 根据类别查询
     *
     * @param category
     * @return
     */
    List<SysDictVo> selectSysDictVoListByCategory(String category);

    /**
     * 更新字典详情
     *
     * @param sysDict
     * @return
     */
    void updateByPrimaryKey(SysDict sysDict);
}
