package com.highershine.portal.common.entity.po;

import lombok.Data;

import java.io.Serializable;

/**
 * @Description: 字典类型
 * @Author: xueboren
 * @Date: 2019/11/27 19:20
 */
@Data
public class SysDictCategory implements Serializable {
    //主键ID
    private Long id;

    //字典类型
    private String dictCategoryCode;

    //字典名称
    private String dictCategoryName;

    //字典描述
    private String dictCategoryDesc;

    //排序字段
    private Long ord;
}
