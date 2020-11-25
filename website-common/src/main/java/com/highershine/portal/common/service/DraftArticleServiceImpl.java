package com.highershine.portal.common.service;


import com.highershine.portal.common.entity.dto.DraftArticleDTO;
import com.highershine.portal.common.entity.po.Category;
import com.highershine.portal.common.entity.po.DraftArticle;
import com.highershine.portal.common.entity.vo.DraftArticleVo;
import com.highershine.portal.common.mapper.CategoryMapper;
import com.highershine.portal.common.mapper.DraftArticleMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @Description 文章草稿业务层接口实现类
 * @Author zxk
 * @Date 2020/4/20 13:00
 **/
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class DraftArticleServiceImpl implements DraftArticleService {
    @Autowired
    private DraftArticleMapper draftArticleMapper;
    @Autowired
    private CategoryMapper categoryMapper;
    /**
     * 获取文章草稿列表
     * @param draftArticleDTO
     * @return
     */
    @Override
    public List<DraftArticleVo> getDraftArticleList(DraftArticleDTO draftArticleDTO) throws Exception {
        List<DraftArticleVo> draftArticleVoList = draftArticleMapper.getDraftArticleList(draftArticleDTO);
        return draftArticleVoList;
    }

    /**
     * 获取文章草稿详情
     * @param id
     * @return
     */
    @Override
    public DraftArticleVo findDraftArticleById(Long id) throws Exception {
        DraftArticleVo draftArticleVo =  draftArticleMapper.findDraftArticleById(id);
        Category category = categoryMapper.selectByPrimaryKey(draftArticleVo.getCategoryId());
        draftArticleVo.setCategoryName(category.getName());
        return draftArticleVo;
    }

    @Override
    public DraftArticleVo addDraftArticle(DraftArticleDTO draftArticleDTO) throws Exception {
        DraftArticle draftArticle = new DraftArticle();
        BeanUtils.copyProperties(draftArticle, draftArticleDTO);
        draftArticleMapper.insert(draftArticle);
        DraftArticleVo draftArticleVo = new DraftArticleVo();
        BeanUtils.copyProperties(draftArticleVo, draftArticle);
        return draftArticleVo;
    }
}
