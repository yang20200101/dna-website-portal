package com.highershine.portal.common.service;


import com.highershine.portal.common.entity.dto.DraftArticleDTO;
import com.highershine.portal.common.entity.po.Article;
import com.highershine.portal.common.entity.po.Category;
import com.highershine.portal.common.entity.po.DraftArticle;
import com.highershine.portal.common.entity.vo.DraftArticleVo;
import com.highershine.portal.common.mapper.ArticleMapper;
import com.highershine.portal.common.mapper.CategoryMapper;
import com.highershine.portal.common.mapper.DraftArticleMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
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
    private ArticleMapper articleMapper;
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
        //保存草稿
        DraftArticle draftArticle = new DraftArticle();
        BeanUtils.copyProperties(draftArticleDTO, draftArticle);
        if (draftArticleDTO.getThumbnail() != null) {
            draftArticle.setThumbnailId(draftArticleDTO.getThumbnail().getId());
        }
        draftArticle.setCreatedAt(new Date()).setUpdatedAt(new Date()).setDeleted(false);
        if (draftArticleDTO.getIsPublish()) {
            //文章 已更新
            draftArticle.setIsNeedUpdate(false);
        } else {
            //草稿 待更新
            draftArticle.setIsNeedUpdate(true);
        }
        draftArticleMapper.insert(draftArticle);
        if (draftArticleDTO.getIsPublish()) {
            //发布文章
            Article article = new Article();
            BeanUtils.copyProperties(draftArticle, article, "id");
            article.setDraftId(draftArticle.getId());
            articleMapper.insert(article);
        }
        //如果已发布
        DraftArticleVo draftArticleVo = new DraftArticleVo();
        BeanUtils.copyProperties(draftArticle, draftArticleVo);
        return draftArticleVo;
    }

    @Override
    public void batchPublish(List<Long> idList) throws Exception {
        for (Long id : idList) {
            //更新草稿表状态
            DraftArticle draftArticle = new DraftArticle();
            draftArticle.setId(id).setIsPublish(true).setIsNeedUpdate(false).setUpdatedAt(new Date()).setPublishDate(new Date());
            draftArticleMapper.updateByPrimaryKeySelective(draftArticle);
            //将草稿表数据 同步到 文章表
            Article article = articleMapper.selectByDraftId(id);
            DraftArticle src = draftArticleMapper.selectByPrimaryKey(id);
            if (article == null) {
                article = new Article();
                BeanUtils.copyProperties(src, article, "id");
                article.setDraftId(id);
                articleMapper.insert(article);
            } else {
                BeanUtils.copyProperties(src, article, "id");
                articleMapper.updateByPrimaryKey(article);
            }
        }
    }

    @Override
    public void batchUnpublish(List<Long> idList) throws Exception {
        for (Long id : idList) {
            //更新草稿表状态
            DraftArticle draftArticle = new DraftArticle();
            draftArticle.setId(id).setIsPublish(false).setUpdatedAt(new Date());
            draftArticleMapper.updateByPrimaryKeySelective(draftArticle);
            //将文章表 数据删除
            articleMapper.deleteFlagByDraftId(id);
        }
    }
}
