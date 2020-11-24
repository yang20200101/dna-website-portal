package com.highershine.portal.common.mapper;


import com.highershine.portal.common.entity.po.Category;
import com.highershine.portal.common.entity.vo.CategoryVo;

import java.util.List;

/**
 * @Description
 * @Author zxk
 * @Date 2020/4/15 15:07
 **/
public interface CategoryMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Category record);

    Category selectByPrimaryKey(Long id);

    int updateByPrimaryKey(Category record);

    /**
     * 查询栏目列表
     * @return
     */
    List<CategoryVo> getCategoryList();

    /**
     * 根据主键删除
     * @param id
     */
    void deleteCategoryById(Long id);

    /**
     * 获取栏目详情
     * @param id
     * @return
     */
    CategoryVo findCategoryById(Long id);
}
