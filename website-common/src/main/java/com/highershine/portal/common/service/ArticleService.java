package com.highershine.portal.common.service;


import com.highershine.portal.common.entity.dto.ArticleDTO;
import com.highershine.portal.common.entity.vo.ArticleVo;

import java.util.List;

/**
 * @Description 文章业务层接口
 * @Author zxk
 * @Date 2020/4/16 15:38
 **/
public interface ArticleService {
    /**
     * 获取文章列表
     * @return
     * @param articleDTO
     */
    List<ArticleVo> getArticleList(ArticleDTO articleDTO) throws Exception;

    /**
     * 根据ID查询文章详情
     * @param id
     * @return
     */
    ArticleVo findArticleById(Long id) throws Exception;
}
