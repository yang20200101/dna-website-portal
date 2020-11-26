package com.highershine.portal.common.service;


import com.highershine.portal.common.entity.dto.CategoryDTO;
import com.highershine.portal.common.entity.vo.CategoryVo;

import java.util.List;

/**
 * @Description 栏目业务层接口
 * @Author zxk
 * @Date 2020/4/15 15:37
 **/
public interface CategoryService {
    /**
     * 查询栏目列表
     * @return
     */
    List<CategoryVo> getCategoryList() throws Exception;

    /**
     * 保存
     * @param categoryDTO
     * @return
     * @throws Exception
     */
    CategoryVo saveCategoryDTO(CategoryDTO categoryDTO) throws Exception;

    /**
     * 删除
     * @param id
     */
    void deleteCategoryById(Long id) throws Exception;

    /**
     * 更新
     * @param categoryDTO
     * @return
     */
    CategoryVo updateCategory(CategoryDTO categoryDTO) throws Exception;

    /**
     * 根据ID获取 栏目详情
     * @param id
     * @return
     */
    CategoryVo findCategoryById(Long id) throws Exception;

    /**
     * 栏目名称别名校验
     * @param categoryDTO
     * @return
     */
    String uniqueValid(CategoryDTO categoryDTO);
}
