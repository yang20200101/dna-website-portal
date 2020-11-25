package com.highershine.portal.common.service;


import com.highershine.portal.common.entity.dto.CategoryDTO;
import com.highershine.portal.common.entity.po.Category;
import com.highershine.portal.common.entity.vo.CategoryVo;
import com.highershine.portal.common.mapper.CategoryMapper;
import com.highershine.portal.common.utils.DateTools;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @Description
 * @Author zxk
 * @Date 2020/4/15 15:37
 **/
@Service
@Transactional(rollbackFor = Exception.class)
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    private CategoryMapper categoryMapper;
    /**
     * 查询栏目列表
     * @return
     */
    @Override
    public List<CategoryVo> getCategoryList() throws Exception {
        List<CategoryVo> categoryVoList =  categoryMapper.getCategoryList();
        return categoryVoList;
    }

    /**
     * 保存
     * @param categoryDTO
     * @return
     */
    @Override
    public CategoryVo saveCategoryDTO(CategoryDTO categoryDTO) throws Exception {
        Category category = new Category();
        BeanUtils.copyProperties(categoryDTO, category);
        //设置创建日期
        category.setCreatedAt(DateTools.getNow());
        //保存
        categoryMapper.insert(category);
        CategoryVo categoryVo = new CategoryVo();
        BeanUtils.copyProperties(category, categoryVo);
        return categoryVo;
    }

    /**
     * 根据主键删除栏目
     * @param id
     */
    @Override
    public void deleteCategoryById(Long id) throws Exception {
        categoryMapper.deleteCategoryById(id);
    }

    /**
     * 更新
     * @param categoryDTO
     * @return
     */
    @Override
    public CategoryVo updateCategory(CategoryDTO categoryDTO) throws Exception {
        Category category = new Category();
        BeanUtils.copyProperties(categoryDTO, category);
        categoryMapper.updateByPrimaryKey(category);
        //根据ID 将更新后的对象查询出来
        category = categoryMapper.selectByPrimaryKey(category.getId());
        CategoryVo categoryVo = new CategoryVo();
        BeanUtils.copyProperties(category, categoryVo);
        return categoryVo;
    }

    /**
     * 根据ID获取栏目详情
     * @param id
     * @return
     * @throws Exception
     */
    @Override
    public CategoryVo findCategoryById(Long id) throws Exception {
      CategoryVo categoryVo =  categoryMapper.findCategoryById(id);
        return categoryVo;
    }
}
