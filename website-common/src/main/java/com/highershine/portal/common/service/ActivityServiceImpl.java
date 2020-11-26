package com.highershine.portal.common.service;


import com.highershine.portal.common.entity.dto.ArticleDTO;
import com.highershine.portal.common.entity.vo.ActivityListVo;
import com.highershine.portal.common.mapper.ActivityMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author xueboren
 * @Date 2020/11/26 15:16
 * @Description TODO
 */
@Slf4j
@Service
public class ActivityServiceImpl implements ActivityService {
    @Resource
    private ActivityMapper activityMapper;

    @Override
    public List<ActivityListVo> getActivityList(ArticleDTO articleDTO) {
        return activityMapper.getActivityList(articleDTO);
    }

    @Override
    public void deleteTrueByPrimaryKey(Long id) {
        activityMapper.deleteTrueByPrimaryKey(id);
    }
}
