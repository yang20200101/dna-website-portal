package com.highershine.portal.common.mapper;


import com.highershine.portal.common.entity.po.ActivityUser;
import com.highershine.portal.common.entity.vo.ActivityPlayerVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Description
 * @Author xueboren
 * @Date 2020/11/27 14:27
 **/
@Repository
public interface ActivityUserMapper {
    int deleteByActivityId(Long id);

    int insert(ActivityUser record);

    List<ActivityUser> selectByActivityId(Long id);

    /**
     * 查询参与者
     * @param id
     * @return
     */
    List<ActivityPlayerVo> selectPlayersByActivityId(long id);

    /**
     * 查询是否有权限报名
     * @param activityId
     * @param userId
     * @return
     */
    int selectPowerNum(@Param("activityId") Long activityId, @Param("userId") Long userId);
}
