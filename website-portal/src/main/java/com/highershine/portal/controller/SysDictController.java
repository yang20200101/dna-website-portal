package com.highershine.portal.controller;

import com.highershine.portal.common.entity.vo.SysDictVo;
import com.highershine.portal.common.enums.ExceptionEnum;
import com.highershine.portal.common.enums.ResultEnum;
import com.highershine.portal.common.result.Result;
import com.highershine.portal.common.service.SysDictCategoryService;
import com.highershine.portal.common.service.SysDictService;
import com.highershine.portal.common.utils.ResultUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * @Description: 字段接口类
 * @Author: mizhanlei
 * @Date: 2019/11/28 19:54
 */
@Slf4j
@RestController
@RequestMapping("sd")
@Api(description = "字典管理相关接口")
public class SysDictController {

    @Autowired
    private SysDictService sysDictService;

    @Autowired
    private SysDictCategoryService sysDictCategoryService;

    /**
     * 查询全部字典列表
     *
     * @return
     */
    @GetMapping("getDictList")
    @ApiOperation("查询全部字典列表(米占磊)")
    public Result<Map<String, List<SysDictVo>>> getSysDictList() {
        Map<String, List<SysDictVo>> sysDictCategory;
        try {
            // 执行查询
            sysDictCategory = this.sysDictCategoryService.querySysDictCategoryList();
            return ResultUtil.successResult(ResultEnum.SUCCESS_STATUS, sysDictCategory);
        } catch (Exception e) {
            log.error("【查询字典】查询全部字典信息时出错， 异常信息：{}", e);
            return ResultUtil.errorResult(ExceptionEnum.UNKNOWN_EXCEPTION);
        }
    }

    /**
     * 根据类别查询字典列表
     *
     * @return
     */
    @ApiOperation("根据类别查询字典列表(米占磊)")
    @GetMapping("getDictList/{category}")
    public Result<List<SysDictVo>> getSysDictListByCategory(
            @ApiParam(value = "字典类别",
                    example = "GENDER",
                    required = true)
            @PathVariable("category") String category, HttpServletRequest request) {
        List<SysDictVo> list;
        try {
            // 执行查询
            list = this.sysDictService.selectSysDictVoListByCategory(category);
            return ResultUtil.successResult(ResultEnum.SUCCESS_STATUS, list);
        } catch (Exception e) {
            log.error("【查询字典】根据类别查询字典列表时出错， 异常信息：{}", e);
            return ResultUtil.errorResult(ExceptionEnum.UNKNOWN_EXCEPTION);
        }
    }

}
