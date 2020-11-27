package com.highershine.portal.common.service;


import com.highershine.portal.common.entity.dto.ActivityDTO;
import com.highershine.portal.common.entity.dto.ArticleDTO;
import com.highershine.portal.common.entity.po.Activity;
import com.highershine.portal.common.entity.po.ActivityUser;
import com.highershine.portal.common.entity.po.Application;
import com.highershine.portal.common.entity.po.Thumbnail;
import com.highershine.portal.common.entity.vo.ActivityListVo;
import com.highershine.portal.common.entity.vo.ActivityPlayerVo;
import com.highershine.portal.common.entity.vo.ActivityUserVo;
import com.highershine.portal.common.entity.vo.ActivityVo;
import com.highershine.portal.common.mapper.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
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
    private SysRegionalismService regionalismService;
    @Resource
    private ActivityMapper activityMapper;
    @Resource
    private ApplicationMapper applicationMapper;
    @Resource
    private ActivityUserMapper activityUserMapper;
    @Resource
    private SysUserMapper sysUserMapper;
    @Resource
    private ThumbnailMapper thumbnailMapper;

    @Override
    public List<ActivityListVo> getActivityList(ArticleDTO articleDTO) {
        return activityMapper.getActivityList(articleDTO);
    }

    @Override
    public void delActivity(Long id) {
        activityMapper.deleteTrueByPrimaryKey(id);
        applicationMapper.deleteTrueByActivityId(id);
    }

    @Override
    public ActivityVo findActivityById(long id) {
        Activity activity = activityMapper.selectByPrimaryKey(id);
        ActivityVo activityVo = new ActivityVo();
        BeanUtils.copyProperties(activity, activityVo);
        Thumbnail thumbnail = thumbnailMapper.selectByPrimaryKey(activity.getThumbnailId());
        activityVo.setThumbnail(thumbnail);
        return activityVo;
    }

    @Override
    public List<ActivityPlayerVo> findPlays() {
        return sysUserMapper.getActivityPlayerList();
    }

    @Override
    public List<ActivityUserVo> findActivityUserById(long id) {
        Application application = new Application();
        application.setActivityId(id).setIsLatest(true);
        //最新报名列表
        List<ActivityUserVo> latestList = activityMapper.getActivityUserList(application);
        application.setIsLatest(false);
        for (ActivityUserVo vo : latestList) {
            //非最新包名列表
            application.setUserId(vo.getUserId());
            List<ActivityUserVo> unLatestList = activityMapper.getActivityUserList(application);
            //省份名称
            vo.setProvince(regionalismService.getNameByCode(vo.getProvince()));
            unLatestList.stream().forEach(info -> info.setProvince(regionalismService.getNameByCode(info.getProvince())));
            vo.setUserList(unLatestList);
        }
        return latestList;
    }

    @Override
    public void insertActivity(ActivityDTO dto) {
        //新增活动
        Activity activity = new Activity();
        BeanUtils.copyProperties(dto, activity);
        activity.setApplyNumber(dto.getUserIdList().size()).setDeleted(false).setCreatedAt(new Date()).setUpdatedAt(new Date());
        activityMapper.insert(activity);
        //新增活动参与人
        for (Long userId : dto.getUserIdList()) {
            ActivityUser activityUser = new ActivityUser();
            activityUser.setUserId(userId).setActivityId(activity.getId());
            activityUserMapper.insert(activityUser);
        }
    }

    @Override
    public void updateActivity(ActivityDTO dto) {
        //修改活动
        Activity activity = new Activity();
        BeanUtils.copyProperties(dto, activity);
        activity.setApplyNumber(dto.getUserIdList().size()).setUpdatedAt(new Date());
        activityMapper.updateByPrimaryKey(activity);
        //新增活动参与人
        activityUserMapper.deleteByActivityId(dto.getId());
        for (Long userId : dto.getUserIdList()) {
            ActivityUser activityUser = new ActivityUser();
            activityUser.setUserId(userId).setActivityId(dto.getId());
            activityUserMapper.insert(activityUser);
        }
    }

    @Override
    public void activityEnroll(Long activityId, Long thumbnailId) {
        //修改曾经上传的报名表状态为 非最新
        Application application = new Application();
        application.setActivityId(activityId).setIsLatest(false);
        applicationMapper.updateIsLatestByActivityId(application);
        //TODO  测试保存随机用户
        List<ActivityUser> list = activityUserMapper.selectByActivityId(activityId);
        Long userId = 0L;
        if (CollectionUtils.isNotEmpty(list)) {
            userId = list.get(0).getUserId();
        }
        //保存最新的报名表信息
        application.setUserId(userId).setThumbnailId(thumbnailId).setIsLatest(false);
        application.setIsLatest(true).setDeleted(false).setUpdatedAt(new Date());
        applicationMapper.insert(application);
    }
}
