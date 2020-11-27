package com.highershine.portal.common.service;


import com.highershine.portal.common.entity.dto.ActivityDTO;
import com.highershine.portal.common.entity.dto.ArticleDTO;
import com.highershine.portal.common.entity.vo.ActivityListVo;
import com.highershine.portal.common.entity.vo.ActivityPlayerVo;
import com.highershine.portal.common.entity.vo.ActivityUserVo;
import com.highershine.portal.common.entity.vo.ActivityVo;

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
    void delActivity(Long id);

    /**
     * 查询活动详细信息
     * @param id
     * @return
     */
    ActivityVo findActivityById(long id);

    /**
     * 查询活动参与者
     * @return
     */
    List<ActivityPlayerVo> findPlays();

    /**
     * 查询活动报名信息
     * @param id
     * @return
     */
    List<ActivityUserVo> findActivityUserById(long id);

    /**
     * 新增活动
     * @param dto
     */
    void insertActivity(ActivityDTO dto);

    /**
     * 修改活动
     * @param dto
     */
    void updateActivity(ActivityDTO dto);

    /**
     * 活动报名
     * @param activityId
     * @param thumbnailId
     */
    void activityEnroll(Long activityId, Long thumbnailId);
}
