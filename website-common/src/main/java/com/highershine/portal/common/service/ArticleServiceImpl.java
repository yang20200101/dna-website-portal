package com.highershine.portal.common.service;


import com.highershine.portal.common.entity.dto.ArticleDTO;
import com.highershine.portal.common.entity.po.Article;
import com.highershine.portal.common.entity.po.Thumbnail;
import com.highershine.portal.common.entity.vo.ArticleVo;
import com.highershine.portal.common.entity.vo.CategoryVo;
import com.highershine.portal.common.entity.vo.ThumbnailVo;
import com.highershine.portal.common.mapper.ArticleMapper;
import com.highershine.portal.common.mapper.CategoryMapper;
import com.highershine.portal.common.mapper.ThumbnailMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
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
    private ThumbnailMapper thumbnailMapper;
    @Autowired
    private CategoryMapper categoryMapper;

    /**
     * 获取文章列表
     *
     * @param endPoint
     * @param bucketName
     * @param articleDTO
     * @return
     */
    @Override
    public List<ArticleVo> getArticleList(String endPoint, String bucketName, ArticleDTO articleDTO) throws Exception {
        List<ArticleVo> voList = articleMapper.getArticleList(articleDTO);
        for (ArticleVo vo : voList) {
            //封装缩略图对象
            ThumbnailVo thumbnail = new ThumbnailVo();
            if (vo.getThumbnailId() != null) {
                Thumbnail img = thumbnailMapper.selectByPrimaryKey(vo.getThumbnailId());
                thumbnail.setId(img.getId());
                thumbnail.setUrl(endPoint + "/" + bucketName + "/" + img.getUrl());
            }
            vo.setThumbnail(thumbnail);
        }
        return voList;
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
        CategoryVo category = categoryMapper.selectVoByPrimaryKey(article.getCategoryId());
        articleVo.setCategory(category);
        return articleVo;
    }
}
