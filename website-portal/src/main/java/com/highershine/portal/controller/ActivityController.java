package com.highershine.portal.controller;


import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.highershine.portal.common.entity.dto.ArticleDTO;
import com.highershine.portal.common.entity.vo.ActivityListVo;
import com.highershine.portal.common.enums.ExceptionEnum;
import com.highershine.portal.common.enums.ResultEnum;
import com.highershine.portal.common.result.Result;
import com.highershine.portal.common.service.ActivityService;
import com.highershine.portal.common.utils.ResultUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Description 活动Controller
 * @Author xueboren
 * @Date 2020/11/26 15:29
 **/
@RestController
@Slf4j
@RequestMapping("activity")
@Api("活动相关接口")
public class ActivityController {

    @Autowired
    private ActivityService activityService;

    /**
     * 获取活动列表
     * @param articleDTO
     * @return
     */
    @PostMapping("list")
    @ApiOperation("查询活动列表（薛博仁）")
    public Result<PageInfo<ActivityListVo>> getActivityList(@RequestBody(required = false) ArticleDTO articleDTO) {
        if (articleDTO == null) {
            articleDTO = new ArticleDTO();
        }
        try {
            PageHelper.startPage(articleDTO.getCurrent(), articleDTO.getPageSize());
            List<ActivityListVo> voList = activityService.getActivityList(articleDTO);
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
    public Result delCategory(
            @ApiParam(value = "主键ID",
                    required = true,
                    example = "1")
            @PathVariable("id") Long id) {
        try {
            activityService.deleteTrueByPrimaryKey(id);
        } catch (Exception e) {
            log.error("【活动】删除异常，异常信息：", e);
            return ResultUtil.errorResult(ExceptionEnum.UNKNOWN_EXCEPTION);
        }
        return ResultUtil.successResult(ResultEnum.SUCCESS_STATUS);
    }
}
