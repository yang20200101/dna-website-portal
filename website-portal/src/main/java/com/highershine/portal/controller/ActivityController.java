package com.highershine.portal.controller;


import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.highershine.portal.common.entity.dto.ActivityDTO;
import com.highershine.portal.common.entity.po.Thumbnail;
import com.highershine.portal.common.entity.vo.*;
import com.highershine.portal.common.enums.ExceptionEnum;
import com.highershine.portal.common.enums.ResultEnum;
import com.highershine.portal.common.result.Result;
import com.highershine.portal.common.service.ActivityService;
import com.highershine.portal.common.utils.ResultUtil;
import com.highershine.portal.config.MinIOPropertyConfig;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * @Description 活动Controller
 * @Author xueboren
 * @Date 2020/11/26 15:29
 **/
@RestController
@Slf4j
@RequestMapping("activity")
@Api(description = "活动相关接口")
public class ActivityController {
    @Autowired
    private ActivityService activityService;
    @Autowired
    private MinIOPropertyConfig minIOPropertyConfig;

    /**
     * 获取活动列表
     * @param dto
     * @return
     */
    @PostMapping("list")
    @ApiOperation("查询活动列表（薛博仁）")
    public Result<PageInfo<ActivityListVo>> getActivityList(@RequestBody(required = false) ActivityDTO dto) {
        if (dto == null) {
            dto = new ActivityDTO();
        }
        try {
            PageHelper.startPage(dto.getCurrent(), dto.getPageSize());
            List<ActivityListVo> voList = activityService.getActivityList(dto);
            PageInfo<ActivityListVo> pageInfo = new PageInfo<>(voList);
            return ResultUtil.successResult(ResultEnum.SUCCESS_STATUS, pageInfo);
        } catch (Exception e) {
            log.error("【活动】查询活动列表异常，异常信息：", e);
            return ResultUtil.errorResult(ExceptionEnum.UNKNOWN_EXCEPTION);
        }
    }

    /**
     * 根据主键ID删除活动
     *
     * @param id
     * @return
     */
    @GetMapping("del/{id}")
    @ApiOperation("根据主键ID删除活动（薛博仁）")
    public Result delActivity(
            @ApiParam(value = "主键ID",
                    required = true,
                    example = "1")
            @PathVariable("id") Long id) {
        try {
            activityService.delActivity(id);
        } catch (Exception e) {
            log.error("【活动】删除异常，异常信息：", e);
            return ResultUtil.errorResult(ExceptionEnum.UNKNOWN_EXCEPTION);
        }
        return ResultUtil.successResult(ResultEnum.SUCCESS_STATUS);
    }

    @GetMapping("find/{id}")
    @ApiOperation("查询详细信息（薛博仁）")
    public Result<ActivityVo> findActivityById(@PathVariable("id") long id) {
        try {
            ActivityVo vo = activityService.findActivityById(id);
            Thumbnail thumbnail = vo.getThumbnail();
            if (thumbnail != null) {
                thumbnail.setUrl(minIOPropertyConfig.getEndPoint() + "/" + minIOPropertyConfig.getBucketName()
                        + "/" + thumbnail.getUrl());
            }
            return ResultUtil.successResult(ResultEnum.SUCCESS_STATUS, vo);
        } catch (Exception e) {
            log.error("【活动】查询活动详情异常，异常信息：", e);
            return ResultUtil.errorResult(ExceptionEnum.UNKNOWN_EXCEPTION);
        }
    }

    @GetMapping("user/find/{id}")
    @ApiOperation("查询活动报名信息（薛博仁）")
    public Result<List<ActivityUserVo>> findActivityUserById(@PathVariable("id") long id) {
        try {
            List<ActivityUserVo> list = activityService.findActivityUserById(id);
            return ResultUtil.successResult(ResultEnum.SUCCESS_STATUS, list);
        } catch (Exception e) {
            log.error("【活动】查询活动详情异常，异常信息：", e);
            return ResultUtil.errorResult(ExceptionEnum.UNKNOWN_EXCEPTION);
        }
    }

    @GetMapping("players")
    @ApiOperation("查询活动参与者（薛博仁）")
    public Result<List<ActivityPlayerVo>> findPlayers() {
        try {
            List<ActivityPlayerVo> list = activityService.findPlays();
            return ResultUtil.successResult(ResultEnum.SUCCESS_STATUS, list);
        } catch (Exception e) {
            log.error("【活动】查询活动参与者异常，异常信息：", e);
            return ResultUtil.errorResult(ExceptionEnum.UNKNOWN_EXCEPTION);
        }
    }

    @PostMapping("insert")
    @ApiOperation("新增活动（薛博仁）")
    public Result<ActivityVo> insertActivity(@Valid @RequestBody ActivityDTO dto, BindingResult bindingResult) {
        try {
            if (bindingResult.hasErrors()) {
                String message = bindingResult.getFieldError().getDefaultMessage();
                return ResultUtil.errorResult(ExceptionEnum.ERROR_PARAMETERS.getCode(), message);
            }
            ActivityVo vo = activityService.insertActivity(dto);
            return ResultUtil.successResult(ResultEnum.SUCCESS_STATUS, vo);
        } catch (Exception e) {
            log.error("【活动】新增活动异常，异常信息：", e);
            return ResultUtil.errorResult(ExceptionEnum.UNKNOWN_EXCEPTION);
        }
    }

    @PostMapping("update")
    @ApiOperation("修改活动（薛博仁）")
    public Result updateActivity(@Valid @RequestBody ActivityDTO dto, BindingResult bindingResult) {
        try {
            String message = "";
            if (bindingResult.hasErrors()) {
                message = bindingResult.getFieldError().getDefaultMessage();
            } else if (dto.getId() == null) {
                message = "活动id为空;";
            }
            if (StringUtils.isNotBlank(message)) {
                return ResultUtil.errorResult(ExceptionEnum.ERROR_PARAMETERS.getCode(), message);
            }
            activityService.updateActivity(dto);
            return ResultUtil.successResult(ResultEnum.SUCCESS_STATUS);
        } catch (Exception e) {
            log.error("【活动】修改活动异常，异常信息：", e);
            return ResultUtil.errorResult(ExceptionEnum.UNKNOWN_EXCEPTION);
        }
    }

    @GetMapping("enroll/{activityId}/{thumbnailId}")
    @ApiOperation("活动报名（薛博仁）")
    public Result activityEnroll(@PathVariable("activityId") Long activityId, @PathVariable("thumbnailId") Long thumbnailId) {
        try {
            //活动报名
            activityService.activityEnroll(activityId, thumbnailId);
            return ResultUtil.successResult(ResultEnum.SUCCESS_STATUS);
        } catch (Exception e) {
            log.error("【活动】活动报名异常，异常信息：", e);
            return ResultUtil.errorResult(ExceptionEnum.UNKNOWN_EXCEPTION);
        }
    }

    @GetMapping("enroll/valid/{activityId}")
    @ApiOperation("活动报名校验（薛博仁）")
    public Result<ActivityEnrollValidVo> activityEnrollValid(@PathVariable("activityId") Long activityId) {
        try {
            ActivityEnrollValidVo vo = activityService.activityEnrollValid(activityId);
            return ResultUtil.successResult(ResultEnum.SUCCESS_STATUS, vo);
        } catch (Exception e) {
            log.error("【活动】活动报名校验异常，异常信息：", e);
            return ResultUtil.errorResult(ExceptionEnum.UNKNOWN_EXCEPTION);
        }
    }
}
