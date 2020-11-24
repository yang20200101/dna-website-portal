package com.highershine.portal.common.service;


import com.highershine.portal.common.entity.dto.ArticleDTO;
import com.highershine.portal.common.entity.po.Article;
import com.highershine.portal.common.entity.po.Category;
import com.highershine.portal.common.entity.vo.ArticleVo;
import com.highershine.portal.common.mapper.ArticleMapper;
import com.highershine.portal.common.mapper.CategoryMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @Description
 * @Author zxk
 * @Date 2020/4/16 15:38
 **/
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class ArticleServiceImpl implements ArticleService {
    @Autowired
    private ArticleMapper articleMapper;
    @Autowired
    private CategoryMapper categoryMapper;

    /**
     * 获取文章列表
     * @param articleDTO
     * @return
     */
    @Override
    public List<ArticleVo> getArticleList(ArticleDTO articleDTO) throws Exception {
       List<ArticleVo> articleVoList =  articleMapper.getArticleList(articleDTO);
        return articleVoList;
    }

    /**
     * 根据ID查询文章详情
     * @param id
     * @return
     */
    @Override
    public ArticleVo findArticleById(Long id) throws Exception {
        Article article =  articleMapper.selectByPrimaryKey(id);
        ArticleVo articleVo = new ArticleVo();
        BeanUtils.copyProperties(article, articleVo);
        Category category = categoryMapper.selectByPrimaryKey(article.getCategoryId());
        articleVo.setCategoryName(category.getName());
        return articleVo;
    }
}
