package com.highershine.portal.common.service;

import com.highershine.portal.common.entity.vo.SysDictVo;

import java.util.List;
import java.util.Map;

/**
 * @Description: 字典业务接口
 * @Author: mizhanlei
 * @Date: 2019/11/28 19:56
 */
public interface SysDictCategoryService {


    /**
     * 查询字典信息
     *
     * @return
     */
    Map<String, List<SysDictVo>> querySysDictCategoryList();
}
