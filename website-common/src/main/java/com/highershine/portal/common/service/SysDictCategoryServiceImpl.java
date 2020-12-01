package com.highershine.portal.common.service;

import com.highershine.portal.common.entity.po.SysDictCategory;
import com.highershine.portal.common.entity.vo.SysDictVo;
import com.highershine.portal.common.mapper.SysDictCategoryMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description: 字典类别业务逻辑层
 * @Author: mizhanlei
 * @Date: 2019/11/29 13:37
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class SysDictCategoryServiceImpl implements SysDictCategoryService {

    @Autowired
    private SysDictService sysDictService;

    @Autowired
    private SysDictCategoryMapper sysDictCategoryMapper;


    /**
     * 查询字典信息
     *
     * @return
     */
    @Override
    public Map<String, List<SysDictVo>> querySysDictCategoryList() {
        // 结果集
        Map<String, List<SysDictVo>> resultMap = new HashMap<>();

        // 查询字典类别列表
        List<SysDictCategory> categoryList = this.sysDictCategoryMapper.selectSysDictCategoryList();

        // 遍历字典类别列表查询组装字典列表
        categoryList.stream().forEach(e -> resultMap.put(e.getDictCategoryCode(),
                this.sysDictService.selectSysDictVoListByCategory(e.getDictCategoryCode())));

        return resultMap;
    }

}

