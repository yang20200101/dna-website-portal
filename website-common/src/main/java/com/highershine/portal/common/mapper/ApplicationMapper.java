package com.highershine.portal.common.mapper;


import com.highershine.portal.common.entity.po.Application;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * @Description
 * @Author xueboren
 * @Date 2020/11/26 15:57
 **/
@Repository
public interface ApplicationMapper {
    int deleteTrueByActivityId(Long id);

    int insert(Application record);

    int updateIsLatestByActivityId(Application record);

    /**
     * 查询已报名人数
     * @param id
     * @return
     */
    int selectApplyNumber(Long id);

    /**
     * 查询是否报名过
     * @param activityId
     * @param userId
     * @return
     */
    int selectEnrollNum(@Param("activityId") Long activityId, @Param("userId") Long userId);
}
