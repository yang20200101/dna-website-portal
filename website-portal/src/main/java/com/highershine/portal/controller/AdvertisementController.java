package com.highershine.portal.controller;


import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.highershine.portal.common.entity.dto.AdvertisementDTO;
import com.highershine.portal.common.entity.po.Thumbnail;
import com.highershine.portal.common.entity.vo.AdvertisementVo;
import com.highershine.portal.common.enums.ExceptionEnum;
import com.highershine.portal.common.enums.ResultEnum;
import com.highershine.portal.common.result.Result;
import com.highershine.portal.common.service.AdvertisementService;
import com.highershine.portal.common.utils.ResultUtil;
import com.highershine.portal.config.MinIOPropertyConfig;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

/**
 * @Author zxk
 * @Date 2020/11/12 15:18
 * @Description TODO
 */
@Slf4j
@RestController
@RequestMapping("/advertisement")
@Api("飘窗管理相关接口")
public class AdvertisementController {
    @Resource
    private AdvertisementService advertisementService;
    @Resource
    private MinIOPropertyConfig minIOPropertyConfig;

    @PostMapping("list")
    @ApiOperation("获取飘窗列表")
    public Result getAdvertisementList(@RequestBody AdvertisementDTO advertisementDTO) {
        if (advertisementDTO == null) {
            advertisementDTO = new AdvertisementDTO();
        }
        try {
            PageHelper.startPage(advertisementDTO.getCurrent(), advertisementDTO.getPageSize());
            List<AdvertisementVo> advertisementVos = advertisementService.getAdvertisementList(advertisementDTO);
            for (AdvertisementVo vo : advertisementVos) {
                Thumbnail thumbnail = new Thumbnail();
                thumbnail.setId(vo.getThumbnailId());
                thumbnail.setUrl(minIOPropertyConfig.getEndPoint() + "/" + minIOPropertyConfig.getBucketName()
                        + "/" + vo.getUrl());
                vo.setThumbnail(thumbnail);
            }
            PageInfo<AdvertisementVo> advertisementVoPageInfo = new PageInfo<>(advertisementVos);
            return ResultUtil.successResult(ResultEnum.SUCCESS_STATUS, advertisementVoPageInfo);
        } catch (Exception e) {
            log.error("【advertisement】获取飘窗列表异常，异常信息：", e);
            return ResultUtil.errorResult(ExceptionEnum.UNKNOWN_EXCEPTION);
        }
    }

    @GetMapping("del/{id}")
    @ApiOperation("根据ID删除飘窗")
    public Result deleteAdvertisement(@PathVariable("id") long id) {
        try {
            advertisementService.deleteAdvertisement(id);
            return ResultUtil.successResult(ResultEnum.SUCCESS_STATUS);
        } catch (Exception e) {
            log.error("【advertisement】删除异常，异常信息：", e);
            return ResultUtil.errorResult(ExceptionEnum.UNKNOWN_EXCEPTION);
        }
    }

    @PostMapping("add")
    @ApiOperation("新增飘窗")
    public Result addAdvertisement(@Valid @RequestBody AdvertisementDTO advertisementDTO, BindingResult bindingResult) {
        String message = "";
        if (bindingResult.hasErrors()) {
            message = bindingResult.getFieldError().getDefaultMessage();
        }else if (advertisementDTO.getThumbnail() == null || advertisementDTO.getThumbnail().getId() == null) {
            message = "未上传缩略图";
        }
        if (StringUtils.isNotBlank(message)) {
            return ResultUtil.errorResult(ExceptionEnum.ERROR_PARAMETERS.getCode(), message);
        }
        try {
            advertisementService.addAdvertisement(advertisementDTO);
            return ResultUtil.successResult(ResultEnum.SUCCESS_STATUS);
        } catch (Exception e) {
            log.error("【advertisement】新增异常，异常信息：", e);
            return ResultUtil.errorResult(ExceptionEnum.UNKNOWN_EXCEPTION);
        }
    }

    @GetMapping("find/{id}")
    @ApiOperation("查询详细信息")
    public Result findAdvertisementById(@PathVariable("id") long id) {
        try {
            AdvertisementVo advertisementVo = advertisementService.findAdvertisementById(id);
            Thumbnail thumbnail = advertisementVo.getThumbnail();
            if (thumbnail != null) {
                thumbnail.setUrl(minIOPropertyConfig.getEndPoint() + "/" + minIOPropertyConfig.getBucketName()
                        + "/" + thumbnail.getUrl());
            }
            return ResultUtil.successResult(ResultEnum.SUCCESS_STATUS, advertisementVo);
        } catch (Exception e) {
            log.error("【advertisement】查询飘窗信息异常，异常信息：", e);
            return ResultUtil.errorResult(ExceptionEnum.UNKNOWN_EXCEPTION);
        }
    }

    @PostMapping("update")
    @ApiOperation("更新")
    public Result updateAdvertisement(@Valid @RequestBody AdvertisementDTO advertisementDTO, BindingResult bindingResult) {
        String message = "";
        if (bindingResult.hasErrors()) {
            message = bindingResult.getFieldError().getDefaultMessage();
        }else if (advertisementDTO.getThumbnail() == null || advertisementDTO.getThumbnail().getId() == null) {
            message = "未上传缩略图;";
        } else if (advertisementDTO.getId() == null) {
            message = "飘窗id为空;";
        }
        if (StringUtils.isNotBlank(message)) {
            return ResultUtil.errorResult(ExceptionEnum.ERROR_PARAMETERS.getCode(), message);
        }
        try {
            advertisementService.updateAdvertisement(advertisementDTO);
            return ResultUtil.successResult(ResultEnum.SUCCESS_STATUS);
        } catch (Exception e) {
            log.error("【advertisement】编辑异常，异常信息：", e);
            return ResultUtil.errorResult(ExceptionEnum.UNKNOWN_EXCEPTION);
        }
    }
}
