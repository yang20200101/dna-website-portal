package com.highershine.portal.common.mapper;


import com.highershine.portal.common.entity.po.ActivityUser;
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
}
