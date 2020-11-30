package com.highershine.portal.common.mapper;


import com.highershine.portal.common.entity.dto.ActivityDTO;
import com.highershine.portal.common.entity.po.Activity;
import com.highershine.portal.common.entity.po.Application;
import com.highershine.portal.common.entity.vo.ActivityListVo;
import com.highershine.portal.common.entity.vo.ActivityUserVo;
import org.apache.ibatis.annotations.Param;
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
     * @param dto
     * @return
     */
    List<ActivityListVo> getActivityList(ActivityDTO dto);

    /**
     * 查询活动报名列表
     * @param application
     * @return
     */
    List<ActivityUserVo> getActivityUserList(Application application);

    /**
     * 修改已报名人数
     * @param id
     * @param applyNumber
     * @return
     */
    int updateApplyNumber(@Param("id") Long id, @Param("applyNumber") int applyNumber);
}
