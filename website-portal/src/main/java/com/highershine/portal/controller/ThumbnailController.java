package com.highershine.portal.controller;

import com.highershine.portal.common.entity.vo.ThumbnailVo;
import com.highershine.portal.common.enums.ExceptionEnum;
import com.highershine.portal.common.enums.ResultEnum;
import com.highershine.portal.common.result.Result;
import com.highershine.portal.common.service.ThumbnailService;
import com.highershine.portal.common.utils.ResultUtil;
import com.highershine.portal.config.MinIOPropertyConfig;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


/**
 * @Description 门户缩略图上传相关接口
 * @Author zxk
 * @Date 2020/4/20 15:17
 **/
@RestController
@Slf4j
@RequestMapping("thumbnail")
@Api(description = "附件相关接口")
public class ThumbnailController {
    @Autowired
    private ThumbnailService thumbnailService;
    @Autowired
    private MinIOPropertyConfig minIOPropertyConfig;

    @PostMapping("upload")
    @ApiOperation("附件上传接口（朱向坤）")
    public Result<ThumbnailVo> upload(@RequestParam(name = "file")MultipartFile file) {
        ThumbnailVo thumbnailVo = null;
        try {
            if (file != null) {
                thumbnailVo = thumbnailService.upload(file, minIOPropertyConfig.getBucketName());
                String url = thumbnailVo.getUrl();
                thumbnailVo.setUrl(minIOPropertyConfig.getEndPoint() + "/" + minIOPropertyConfig.getBucketName() + "/" + url);
            }
        } catch (Exception e) {
            log.error("【附件】上传异常，异常信息：", e);
            return ResultUtil.errorResult(ExceptionEnum.UNKNOWN_EXCEPTION);
        }
        return ResultUtil.successResult(ResultEnum.SUCCESS_STATUS, thumbnailVo);
    }


    @GetMapping("download/{id}")
    @ApiOperation("附件下载接口（薛博仁）")
    public void download(@PathVariable("id") Long id) {
        try {
            thumbnailService.download(minIOPropertyConfig.getBucketName(), id);
        } catch (Exception e) {
            log.error("【附件】下载出错，异常信息：", e);
        }
    }

    @GetMapping("download/activity/{activityId}")
    @ApiOperation("下载活动报名信息接口（薛博仁）")
    public void downloadActivityInfo(@PathVariable("activityId") Long activityId) {
        try {
            thumbnailService.downloadActivityInfo(minIOPropertyConfig.getBucketName(), activityId);
        } catch (Exception e) {
            log.error("【附件】下载活动报名信息出错，异常信息：", e);
        }
    }
}
