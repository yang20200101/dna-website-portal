package com.highershine.portal.common.mapper;


import com.highershine.portal.common.entity.dto.ArticleDTO;
import com.highershine.portal.common.entity.po.Activity;
import com.highershine.portal.common.entity.po.Application;
import com.highershine.portal.common.entity.vo.ActivityListVo;
import com.highershine.portal.common.entity.vo.ActivityUserVo;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Description
 * @Author xueboren
 * @Date 2020/11/26 15:57
 **/
@Repository
public interface ActivityMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Activity record);

    Activity selectByPrimaryKey(Long id);

    int deleteTrueByPrimaryKey(Long id);

    int updateByPrimaryKey(Activity record);

    /**
     * 查询活动列表
     * @param articleDTO
     * @return
     */
    List<ActivityListVo> getActivityList(ArticleDTO articleDTO);

    /**
     * 查询活动报名列表
     * @param application
     * @return
     */
    List<ActivityUserVo> getActivityUserList(Application application);
}
