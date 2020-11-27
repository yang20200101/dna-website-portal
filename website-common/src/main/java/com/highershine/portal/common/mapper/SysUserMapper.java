package com.highershine.portal.common.mapper;


import com.highershine.portal.common.entity.vo.ActivityPlayerVo;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Description
 * @Author xueboren
 * @Date 2020/11/27 9:48
 **/
@Repository
public interface SysUserMapper {
    /**
     * 查询活动参与人
     * @return
     */
    List<ActivityPlayerVo> getActivityPlayerList();
}
