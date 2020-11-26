package com.highershine.portal.common.service;


import com.highershine.portal.common.entity.dto.ArticleDTO;
import com.highershine.portal.common.entity.vo.ActivityListVo;

import java.util.List;

/**
 * 活动业务层
 */
public interface ActivityService {

    /**
     * 查询活动列表
     * @param articleDTO
     * @return
     */
    List<ActivityListVo> getActivityList(ArticleDTO articleDTO);

    /**
     * 删除活动
     * @param id
     */
    void deleteTrueByPrimaryKey(Long id);
}
