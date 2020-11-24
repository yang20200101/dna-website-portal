package com.highershine.portal.common.service;


import com.highershine.portal.common.entity.dto.DraftArticleDTO;
import com.highershine.portal.common.entity.vo.DraftArticleVo;

import java.util.List;

/**
 * @Description 文章草稿业务层接口
 * @Author zxk
 * @Date 2020/4/20 12:59
 **/
public interface DraftArticleService {
    /**
     * 获取文章草稿列表
     * @param draftArticleDTO
     * @return
     */
    List<DraftArticleVo> getDraftArticleList(DraftArticleDTO draftArticleDTO) throws Exception;

    /**
     * 获取文章草稿详情
     * @param id
     * @return
     */
    DraftArticleVo findDraftArticleById(Long id) throws Exception;

    /**
     * 新增文章
     * @return
     * @throws Exception
     * @param draftArticleDTO
     */
    DraftArticleVo addDraftArticle(DraftArticleDTO draftArticleDTO) throws Exception;
}
