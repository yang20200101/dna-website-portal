package com.highershine.portal.common.service;

import com.highershine.portal.common.entity.po.SysDict;
import com.highershine.portal.common.entity.vo.SysDictVo;

import java.util.List;

/**
 * @Description: 字典业务接口
 * @Author: mizhanlei
 * @Date: 2019/11/28 19:56
 */
public interface SysDictService {
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
     * 更新字典详情
     *
     * @param sysDict
     * @return
     */
    void updateByPrimaryKey(SysDict sysDict);

    /**
     * 根据类别查询
     *
     * @param dictCategoryCode
     * @return
     */
    List<SysDictVo> selectSysDictVoListByCategory(String dictCategoryCode);
}
