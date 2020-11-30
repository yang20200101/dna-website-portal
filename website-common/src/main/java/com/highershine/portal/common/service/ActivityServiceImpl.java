package com.highershine.portal.common.service;


import com.highershine.portal.common.entity.dto.ActivityDTO;
import com.highershine.portal.common.entity.po.Activity;
import com.highershine.portal.common.entity.po.ActivityUser;
import com.highershine.portal.common.entity.po.Application;
import com.highershine.portal.common.entity.po.Thumbnail;
import com.highershine.portal.common.entity.vo.*;
import com.highershine.portal.common.mapper.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Random;

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
    public List<ActivityListVo> getActivityList(ActivityDTO dto) {
        return activityMapper.getActivityList(dto);
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
        //附件
        Thumbnail thumbnail = thumbnailMapper.selectByPrimaryKey(activity.getThumbnailId());
        activityVo.setThumbnail(thumbnail);
        //参与者
        List<ActivityPlayerVo> players = activityUserMapper.selectPlayersByActivityId(id);
        activityVo.setPlayerList(players);
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
            vo.setChildren(unLatestList);
        }
        return latestList;
    }

    @Override
    public ActivityVo insertActivity(ActivityDTO dto) {
        //新增活动
        Activity activity = new Activity();
        BeanUtils.copyProperties(dto, activity);
        activity.setApplyNumber(0).setDeleted(false).setCreatedAt(new Date()).setUpdatedAt(new Date());
        if (dto.getThumbnail() != null) {
            activity.setThumbnailId(dto.getThumbnail().getId());
        }
        activityMapper.insert(activity);
        //新增活动参与人
        for (Long userId : dto.getUserIdList()) {
            ActivityUser activityUser = new ActivityUser();
            activityUser.setUserId(userId).setActivityId(activity.getId());
            activityUserMapper.insert(activityUser);
        }
        //返回id
        ActivityVo vo = new ActivityVo();
        vo.setId(activity.getId());
        return vo;
    }

    @Override
    public void updateActivity(ActivityDTO dto) {
        //修改活动
        Activity activity = new Activity();
        BeanUtils.copyProperties(dto, activity);
        if (dto.getThumbnail() != null) {
            activity.setThumbnailId(dto.getThumbnail().getId());
        }
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
        application.setIsLatest(true).setDeleted(false).setCreatedAt(new Date()).setUpdatedAt(new Date());
        applicationMapper.insert(application);
        //修改已报名人数
        int num = applicationMapper.selectApplyNumber(activityId);
        activityMapper.updateApplyNumber(activityId, num);
    }

    @Override
    public ActivityEnrollValidVo activityEnrollValid(Long activityId) {
        ActivityEnrollValidVo vo = new ActivityEnrollValidVo();
        // TODO   由于用户模块暂未接入，无法获取用户信息， 使用随机结果进行测试
//        Long userId = 0L;
//        int powerNum = activityUserMapper.selectPowerNum(activityId, userId);
//        if (powerNum > 0) {
//            //有权限
//            vo.setPowerFlag(true);
//        }
//        int enrollNum = applicationMapper.selectEnrollNum(activityId, userId);
//        if (enrollNum > 0) {
//            //报名过
//            vo.setEnrollFlag(true);
//        }
        Random ra =new Random();
        int num = ra.nextInt(3)+1;
        if (num == 1) {
            //测试没权限
            vo.setPowerFlag(false);
        } else if (num == 2) {
            //测试报名过
            vo.setPowerFlag(true);
            vo.setEnrollFlag(true);
        } else {
            //测试可以报名
            vo.setPowerFlag(true);
            vo.setEnrollFlag(false);
        }
        return vo;
    }
}
