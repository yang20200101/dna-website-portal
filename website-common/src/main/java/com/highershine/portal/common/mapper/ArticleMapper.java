package com.highershine.portal.common.mapper;


import com.highershine.portal.common.entity.dto.ArticleDTO;
import com.highershine.portal.common.entity.po.Article;
import com.highershine.portal.common.entity.vo.ArticleVo;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArticleMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Article record);

    Article selectByPrimaryKey(Long id);

    Article selectByDraftId(Long id);

    int updateByPrimaryKey(Article record);

    /**
     * 获取文章列表
     * @return
     * @param articleDTO
     */
    List<ArticleVo> getArticleList(ArticleDTO articleDTO);

    /**
     * 根据草稿id删除
     * @param id
     */
    void deleteByDraftId(Long id);
}
