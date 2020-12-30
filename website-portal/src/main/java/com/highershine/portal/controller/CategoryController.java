package com.highershine.portal.controller;


import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.highershine.portal.common.entity.dto.CategoryDTO;
import com.highershine.portal.common.entity.vo.CategoryVo;
import com.highershine.portal.common.enums.ExceptionEnum;
import com.highershine.portal.common.enums.ResultEnum;
import com.highershine.portal.common.result.Result;
import com.highershine.portal.common.service.CategoryService;
import com.highershine.portal.common.utils.ResultUtil;
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
 * @Description
 * @Author zxk
 * @Date 2020/4/15 10:34
 **/
@RestController
@Slf4j
@RequestMapping("category")
@Api(description = "栏目相关接口")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @PostMapping("getCategoryList")
    @ApiOperation("获取栏目列表(朱向坤)")
    public Result<PageInfo<CategoryVo>> getCategoryList(@RequestBody(required = false) CategoryDTO categoryDTO) {
        try {
            if (categoryDTO == null) {
                categoryDTO = new CategoryDTO();
            }
            PageHelper.startPage(categoryDTO.getCurrent(), categoryDTO.getPageSize());
            List<CategoryVo> categoryVoList = categoryService.getCategoryList(categoryDTO);
            PageInfo<CategoryVo> pageInfo = new PageInfo<>(categoryVoList);
            return ResultUtil.successResult(ResultEnum.SUCCESS_STATUS, pageInfo);
        } catch (Exception e) {
            log.error("【栏目】列表查询异常，异常信息：", e);
            return ResultUtil.errorResult(ExceptionEnum.UNKNOWN_EXCEPTION);
        }
    }

    @PostMapping("add")
    @ApiOperation("新增栏目(朱向坤)")
    public Result addCategory(@Valid @RequestBody CategoryDTO categoryDTO, BindingResult bindingResult) {
        //参数校验
        String message = "";
        if (bindingResult.hasErrors()) {
            message = bindingResult.getFieldError().getDefaultMessage();
        } else {
            //栏目名称 和 别名不能重复
            message = categoryService.uniqueValid(categoryDTO);
        }
        if (StringUtils.isNotBlank(message)) {
            return ResultUtil.errorResult(ExceptionEnum.ERROR_PARAMETERS.getCode(), message);
        }

        try {
            CategoryVo categoryVo = categoryService.saveCategoryDTO(categoryDTO);
            return ResultUtil.successResult(ResultEnum.SUCCESS_STATUS, categoryVo);
        } catch (Exception e) {
            log.error("【栏目】新增异常，异常信息：", e);
            return ResultUtil.errorResult(ExceptionEnum.UNKNOWN_EXCEPTION);
        }

    }

    /**
     * 根据主键ID删除栏目
     *
     * @param id
     * @return
     */
    @GetMapping("del/{id}")
    @ApiOperation("根据主键ID删除栏目（朱向坤）")
    public Result delCategory(
            @ApiParam(value = "主键ID",
                    required = true,
                    example = "1")
            @PathVariable("id") Long id) {
        try {
            categoryService.deleteCategoryById(id);
        } catch (Exception e) {
            log.error("【栏目】删除异常，异常信息：", e);
            return ResultUtil.errorResult(ExceptionEnum.UNKNOWN_EXCEPTION);
        }
        return ResultUtil.successResult(ResultEnum.SUCCESS_STATUS);
    }

    /**
     * 更新
     *
     * @param categoryDTO
     * @param bindingResult
     * @return
     */
    @ApiOperation("更新栏目（朱向坤）")
    @PostMapping("update")
    public Result<CategoryVo> updateCategory(@RequestBody CategoryDTO categoryDTO, BindingResult bindingResult) {
        //校验参数
        String message = "";
        if (bindingResult.hasErrors()) {
            message = bindingResult.getFieldError().getDefaultMessage();
        } else if (categoryDTO.getId() == null) {
            message = "栏目id为空;";
        } else {
            //栏目名称 和 别名不能重复
            message = categoryService.uniqueValid(categoryDTO);
        }
        if (StringUtils.isNotBlank(message)) {
            return ResultUtil.errorResult(ExceptionEnum.ERROR_PARAMETERS.getCode(), message);
        }
        try {
            CategoryVo categoryVo = categoryService.updateCategory(categoryDTO);
            return ResultUtil.successResult(ResultEnum.SUCCESS_STATUS, categoryVo);
        } catch (Exception e) {
            log.error("【栏目】更新异常，异常信息：", e);
            return ResultUtil.errorResult(ExceptionEnum.UNKNOWN_EXCEPTION);
        }
    }


    @GetMapping("find/{id}")
    @ApiOperation("根据ID获取栏目详情（朱向坤）")
    public Result<CategoryVo> findCategoryById(@PathVariable("id") Long id) {

        try {
            CategoryVo categoryVo = categoryService.findCategoryById(id);
            return ResultUtil.successResult(ResultEnum.SUCCESS_STATUS, categoryVo);
        } catch (Exception e) {
            log.error("【栏目】获取栏目详情异常，异常信息：", e);
            return ResultUtil.errorResult(ExceptionEnum.UNKNOWN_EXCEPTION);
        }
    }


    @GetMapping("alias/{alias}")
    @ApiOperation("根据别名获取栏目详情（薛博仁）")
    public Result<CategoryVo> findCategoryById(@PathVariable("alias") String alias) {

        try {
            CategoryVo categoryVo = categoryService.findCategoryByAlias(alias);
            return ResultUtil.successResult(ResultEnum.SUCCESS_STATUS, categoryVo);
        } catch (Exception e) {
            log.error("【栏目】根据别名获取栏目详情异常，异常信息：", e);
            return ResultUtil.errorResult(ExceptionEnum.UNKNOWN_EXCEPTION);
        }
    }
}
