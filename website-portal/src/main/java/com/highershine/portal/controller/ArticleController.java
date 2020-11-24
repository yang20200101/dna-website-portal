package com.highershine.portal.controller;


import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.highershine.portal.common.entity.dto.ArticleDTO;
import com.highershine.portal.common.entity.vo.ArticleVo;
import com.highershine.portal.common.enums.ExceptionEnum;
import com.highershine.portal.common.enums.ResultEnum;
import com.highershine.portal.common.result.Result;
import com.highershine.portal.common.service.ArticleService;
import com.highershine.portal.common.utils.ResultUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Description 文章Controller
 * @Author zxk
 * @Date 2020/4/16 15:29
 **/
@RestController
@Slf4j
@RequestMapping("article")
@Api("文章相关接口")
public class ArticleController {

    @Autowired
    private ArticleService articleService;

    /**
     * 获取文章列表
     * @param articleDTO
     * @return
     */
    @PostMapping("getList")
    @ApiOperation("查询文章列表（朱向坤）")
    public Result<PageInfo<ArticleVo>> getArticleList(@RequestBody(required = false) ArticleDTO articleDTO) {
        if (articleDTO == null) {
            articleDTO = new ArticleDTO();
        }
        try {
            PageHelper.startPage(articleDTO.getCurrent(), articleDTO.getPageSize());
            List<ArticleVo> articleVoList = articleService.getArticleList(articleDTO);
            PageInfo<ArticleVo> articleVoPageInfo = new PageInfo<>(articleVoList);
            return ResultUtil.successResult(ResultEnum.SUCCESS_STATUS, articleVoPageInfo);
        } catch (Exception e) {
            log.error("【文章】获取文章列表异常，异常信息：", e);
            return ResultUtil.errorResult(ExceptionEnum.UNKNOWN_EXCEPTION);
        }
    }

    /**
     * 根据ID获取文章详情
     * @param id
     * @return
     */
    @GetMapping("find/{id}")
    @ApiOperation("根据ID获取文章详情（朱向坤）")
    public Result<ArticleVo> findArticleById(@PathVariable("id")Long id) {
        try {
            ArticleVo articleVo =  articleService.findArticleById(id);
            return ResultUtil.successResult(ResultEnum.SUCCESS_STATUS, articleVo);
        } catch (Exception e) {
            log.error("【文章】获取文章详情异常，异常信息：", e);
            return ResultUtil.errorResult(ExceptionEnum.UNKNOWN_EXCEPTION);
        }
    }
}
