package com.highershine.portal.common.mapper;


import com.highershine.portal.common.entity.dto.DraftArticleDTO;
import com.highershine.portal.common.entity.po.DraftArticle;
import com.highershine.portal.common.entity.vo.DraftArticleVo;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DraftArticleMapper {
    int deleteByPrimaryKey(Long id);

    int insert(DraftArticle record);

    DraftArticle selectByPrimaryKey(Long id);

    int updateByPrimaryKey(DraftArticle record);

    /**
     * 获取文章草稿列表
     * @param draftArticleDTO
     * @return
     */
    List<DraftArticleVo> getDraftArticleList(DraftArticleDTO draftArticleDTO);

    /**
     * 获取文章草稿详情
     * @param id
     * @return
     */
    DraftArticleVo findDraftArticleById(Long id);

    int updateByPrimaryKeySelective(DraftArticle record);
}
