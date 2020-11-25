package com.highershine.portal.controller;


import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.highershine.portal.common.entity.dto.DraftArticleDTO;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;

/**
 * @Description 草稿Controller
 * @Author zxk
 * @Date 2020/4/16 16:59
 **/
@Slf4j
@RestController
@RequestMapping("draft")
@Api("文章草稿相关接口")
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
            PageHelper.startPage(draftArticleDTO.getCurrent(), draftArticleDTO.getPageSize());
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
            draftArticleVo.setUrl(minIOPropertyConfig.getEndPoint() + "/" + minIOPropertyConfig.getBucketName()
                    + "/" + draftArticleVo.getUrl());
            return ResultUtil.successResult(ResultEnum.SUCCESS_STATUS, draftArticleVo);
        } catch (Exception e) {
            log.error("【文章草稿】获取文章草稿详情出现异常，异常信息：", e);
            return ResultUtil.errorResult(ExceptionEnum.UNKNOWN_EXCEPTION);
        }
    }

    @ApiOperation("新建文章")
    @PostMapping("add")
    public Result<DraftArticleVo> addDraftArticle(@RequestBody DraftArticleDTO draftArticleDTO, BindingResult bindingResult) {
        //校验参数
        if (bindingResult.hasErrors()) {
            String message = bindingResult.getFieldError().getDefaultMessage();
            return ResultUtil.errorResult(ExceptionEnum.EXISTS_PARAMETERS.getCode(), message);
        }
        try {
            DraftArticleVo draftArticleVo = draftArticleService.addDraftArticle(draftArticleDTO);
            return ResultUtil.successResult(ResultEnum.SUCCESS_STATUS, draftArticleVo);
        } catch (Exception e) {
            log.error("【文章草稿】新增文章异常，异常信息：", e);
            return ResultUtil.errorResult(ExceptionEnum.UNKNOWN_EXCEPTION);
        }
    }

    /**
     * 批量发布
     */
    @PostMapping("publish")
    @ApiOperation("批量发布")
    public Result batchPublish(HttpServletRequest request) {
        String str;
        String whole = "";
        try {
            BufferedReader reader = request.getReader();
            while ((str = reader.readLine()) != null) {
                whole += str;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ResultUtil.successResult(ResultEnum.SUCCESS_STATUS, whole);
    }
}
