package com.highershine.portal.controller;


import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.highershine.portal.common.entity.dto.BaseDTO;
import com.highershine.portal.common.entity.dto.DraftArticleDTO;
import com.highershine.portal.common.entity.po.Thumbnail;
import com.highershine.portal.common.entity.vo.DraftArticleVo;
import com.highershine.portal.common.enums.ExceptionEnum;
import com.highershine.portal.common.enums.ResultEnum;
import com.highershine.portal.common.result.Result;
import com.highershine.portal.common.service.DraftArticleService;
import com.highershine.portal.common.utils.ResultUtil;
import com.highershine.portal.config.MinIOPropertyConfig;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * @Description 草稿Controller
 * @Author zxk
 * @Date 2020/4/16 16:59
 **/
@Slf4j
@RestController
@RequestMapping("draft")
@Api(description = "文章草稿相关接口")
public class DraftArticleController {
    @Autowired
    private DraftArticleService draftArticleService;
    @Autowired
    private MinIOPropertyConfig minIOPropertyConfig;

    @PostMapping("list")
    @ApiOperation("获取文章草稿列表(朱向坤)")
    public Result getList(@RequestBody DraftArticleDTO draftArticleDTO) {
        if (draftArticleDTO == null) {
            draftArticleDTO = new DraftArticleDTO();
        }
        try {
            PageHelper.startPage(new BaseDTO().getCurrent(), new BaseDTO().getPageSize());
            List<DraftArticleVo> draftArticleVoList = draftArticleService.getDraftArticleList(draftArticleDTO);
            PageInfo<DraftArticleVo> draftArticleVoPageInfo = new PageInfo<>(draftArticleVoList);
            return ResultUtil.successResult(ResultEnum.SUCCESS_STATUS, draftArticleVoPageInfo);
        } catch (Exception e) {
            log.error("【文章草稿】获取文章草稿列表异常，异常信息：", e);
            return ResultUtil.errorResult(ExceptionEnum.UNKNOWN_EXCEPTION);
        }
    }

    /**
     * 获取文章草稿详情
     *
     * @param id
     * @return
     */
    @GetMapping("find/{id}")
    @ApiOperation("获取文章草稿详情")
    public Result<DraftArticleVo> findDraftArticleById(@PathVariable("id") Long id) {
        try {
            DraftArticleVo draftArticleVo = draftArticleService.findDraftArticleById(id);
            Thumbnail thumbnail = new Thumbnail();
            if (StringUtils.isNotBlank(draftArticleVo.getUrl())) {
                thumbnail.setUrl(minIOPropertyConfig.getEndPoint() + "/" + minIOPropertyConfig.getBucketName()
                        + "/" + draftArticleVo.getUrl()).setId(draftArticleVo.getThumbnailId());
            }
            draftArticleVo.setThumbnail(thumbnail);
            return ResultUtil.successResult(ResultEnum.SUCCESS_STATUS, draftArticleVo);
        } catch (Exception e) {
            log.error("【文章草稿】获取文章草稿详情出现异常，异常信息：", e);
            return ResultUtil.errorResult(ExceptionEnum.UNKNOWN_EXCEPTION);
        }
    }

    @ApiOperation("新建文章")
    @PostMapping("add")
    public Result<DraftArticleVo> addDraftArticle(@Valid @RequestBody DraftArticleDTO draftArticleDTO, BindingResult bindingResult) {
        //校验参数
        if (bindingResult.hasErrors()) {
            String message = bindingResult.getFieldError().getDefaultMessage();
            return ResultUtil.errorResult(ExceptionEnum.ERROR_PARAMETERS.getCode(), message);
        }
        try {
            DraftArticleVo draftArticleVo = draftArticleService.addDraftArticle(draftArticleDTO);
            return ResultUtil.successResult(ResultEnum.SUCCESS_STATUS, draftArticleVo);
        } catch (Exception e) {
            log.error("【文章草稿】新增文章异常，异常信息：", e);
            return ResultUtil.errorResult(ExceptionEnum.UNKNOWN_EXCEPTION);
        }
    }

    @ApiOperation("修改文章")
    @PostMapping("update")
    public Result updateDraftArticle(@Valid @RequestBody DraftArticleDTO draftArticleDTO, BindingResult bindingResult) {
        try {
            //校验参数
            String message = "";
            if (bindingResult.hasErrors()) {
                message = bindingResult.getFieldError().getDefaultMessage();
            } else if (draftArticleDTO.getId() == null) {
                message = "草稿ID为空;";
            }
            if (StringUtils.isNotBlank(message)) {
                return ResultUtil.errorResult(ExceptionEnum.ERROR_PARAMETERS.getCode(), message);
            }
            draftArticleService.updateDraftArticle(draftArticleDTO);
            return ResultUtil.successResult(ResultEnum.SUCCESS_STATUS);
        } catch (Exception e) {
            log.error("【文章草稿】修改文章异常，异常信息：", e);
            return ResultUtil.errorResult(ExceptionEnum.UNKNOWN_EXCEPTION);
        }
    }

    /**
     * 批量发布
     */
    @PostMapping("publish")
    @ApiOperation("批量发布（薛博仁）")
    public Result batchPublish(@RequestBody List<Long> idList) {
        try {
            draftArticleService.batchPublish(idList);
            return ResultUtil.successResult(ResultEnum.SUCCESS_STATUS);
        } catch (Exception e) {
            log.error("【文章草稿】批量发布异常，异常信息：", e);
            return ResultUtil.errorResult(ExceptionEnum.UNKNOWN_EXCEPTION);
        }
    }

    /**
     * 批量发布
     */
    @PostMapping("unpublish")
    @ApiOperation("批量取消发布（薛博仁）")
    public Result unpublish(@RequestBody List<Long> idList) {
        try {
            draftArticleService.batchUnpublish(idList);
            return ResultUtil.successResult(ResultEnum.SUCCESS_STATUS);
        } catch (Exception e) {
            log.error("【文章草稿】批量取消发布异常，异常信息：", e);
            return ResultUtil.errorResult(ExceptionEnum.UNKNOWN_EXCEPTION);
        }
    }
}
