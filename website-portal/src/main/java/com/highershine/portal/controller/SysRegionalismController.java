package com.highershine.portal.controller;

import com.highershine.portal.common.constants.RegionalismConstant;
import com.highershine.portal.common.entity.vo.SysRegionalismVo;
import com.highershine.portal.common.enums.ExceptionEnum;
import com.highershine.portal.common.enums.ResultEnum;
import com.highershine.portal.common.result.Result;
import com.highershine.portal.common.service.SysRegionalismService;
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

import java.util.List;

/**
 * @Description: 行政区划接口类
 * @Author: mizhanlei
 * @Date: 2019/11/29 15:29
 */
@Slf4j
@RestController
@RequestMapping("region")
@Api(description = "行政区划相关接口")
public class SysRegionalismController {

    @Autowired
    private SysRegionalismService sysRegionalismService;

    /**
     * 获取顶节点行政区划列表
     *
     * @return
     */
    @GetMapping("top")
    @ApiOperation("获取顶节点行政区划列表接口(米占磊)")
    public Result<List<SysRegionalismVo>> getTopSysRegionalism() {
        List<SysRegionalismVo> result;
        try {
            // 获取顶节点行政区划列表
            result = this.sysRegionalismService.selectSysRegionalismVoListByParentCode(
                    RegionalismConstant.TOP_REGIONALISM_PARENT_CODE);
            return ResultUtil.successResult(ResultEnum.SUCCESS_STATUS, result);
        } catch (Exception e) {
            log.error("【行政区划】获取顶节点行政区划时异常， 异常信息：{}", e);
            return ResultUtil.errorResult(ExceptionEnum.UNKNOWN_EXCEPTION);
        }
    }


    /**
     * 根据父行政区划获取子行政区划列表
     *
     * @return
     */
    @GetMapping("getSysRegionalism/{pCode}")
    @ApiOperation("根据父行政区划获取子行政区划列表(米占磊)")
    public Result<List<SysRegionalismVo>> getSysRegionalismByParentCode(
            @ApiParam(value = "父行政区划",
                    allowableValues = "000000, 110100, 130000, 130100, 140000, 140100, 210000, 220000, 330000",
                    required = true)
            @PathVariable("pCode") String pcode) {
        List<SysRegionalismVo> result;
        try {
            // 获取顶节点行政区划列表
            result = this.sysRegionalismService.selectSysRegionalismVoListByParentCode(pcode);
            return ResultUtil.successResult(ResultEnum.SUCCESS_STATUS, result);
        } catch (Exception e) {
            log.error("【行政区划】根据父行政区划获取子行政区划列表时异常， 异常信息：{}", e);
            return ResultUtil.errorResult(ExceptionEnum.UNKNOWN_EXCEPTION);
        }
    }

    /**
     * 获取行政区划树
     *
     * @return
     */
    @GetMapping("getTree")
    @ApiOperation("获取行政区划树接口(米占磊)")
    public Result<List<SysRegionalismVo>> getSysRegionalismTree() {
        List<SysRegionalismVo> result;
        try {
            // 获取行政区划树
            result = this.sysRegionalismService.selectSysRegionalismVoTree();
            return ResultUtil.successResult(ResultEnum.SUCCESS_STATUS, result);
        } catch (Exception e) {
            log.error("【行政区划】获取行政区划树时异常， 异常信息：{}", e);
            return ResultUtil.errorResult(ExceptionEnum.UNKNOWN_EXCEPTION);
        }
    }
}
