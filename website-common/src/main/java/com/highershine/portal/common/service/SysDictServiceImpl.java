package com.highershine.portal.common.service;

import com.highershine.portal.common.entity.po.SysDict;
import com.highershine.portal.common.entity.vo.SysDictVo;
import com.highershine.portal.common.mapper.SysDictMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @Description: 字典业务逻辑层
 * @Author: mizhanlei
 * @Date: 2019/11/28 19:57
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class SysDictServiceImpl implements SysDictService {

    @Autowired
    private SysDictMapper sysDictMapper;


    /**
     * 根据id删除
     *
     * @param id
     * @return
     */
    @Override
    public void deleteByPrimaryKey(Long id) {
        this.sysDictMapper.deleteByPrimaryKey(id);
    }

    /**
     * 插入字典详情
     *
     * @param sysDict
     * @return
     */
    @Override
    public long insertSysDict(SysDict sysDict) {
        return this.sysDictMapper.insertSysDict(sysDict);
    }

    /**
     * 根据id查询
     *
     * @param id
     * @return
     */
    @Override
    public SysDict selectByPrimaryKey(Long id) {
        return this.sysDictMapper.selectByPrimaryKey(id);
    }

    /**
     * 根据类别查询
     *
     * @param category
     * @return
     */
    @Override
    public List<SysDict> selectSysDictListByCategory(String category) {
        return this.sysDictMapper.selectSysDictListByCategory(category);
    }

    /**
     * 根据类别查询
     *
     * @param category
     * @return
     */
    @Override
    public List<SysDictVo> selectSysDictVoListByCategory(String category) {
        return this.sysDictMapper.selectSysDictVoListByCategory(category);
    }

    /**
     * 更新字典详情
     *
     * @param sysDict
     * @return
     */
    @Override
    public void updateByPrimaryKey(SysDict sysDict) {
        this.sysDictMapper.updateByPrimaryKey(sysDict);
    }
}
