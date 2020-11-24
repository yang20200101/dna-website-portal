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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;


/**
 * @Description 门户缩略图上传相关接口
 * @Author zxk
 * @Date 2020/4/20 15:17
 **/
@RestController
@Slf4j
@RequestMapping("thumbnail")
@Api("缩略图相关接口")
public class ThumbnailController {
    @Autowired
    private ThumbnailService thumbnailService;
    @Autowired
    private MinIOPropertyConfig minIOPropertyConfig;

    @PostMapping("upload")
    @ApiOperation("缩略图上传接口（朱向坤）")
    public Result<ThumbnailVo> upload(@RequestParam(name = "file")MultipartFile file) {
        List<String> verify = Arrays.asList(".jpg", ".jpeg", ".png", ".png");
        //校验文件格式
        String filename = file.getOriginalFilename();
        String suffix = filename.substring(filename.indexOf("."));
        if (!verify.contains(suffix) || file.isEmpty()) {
            log.error("【缩略图】上传异常，文件格式不支持！");
            return ResultUtil.errorResult(ExceptionEnum.EXISTS_PARAMETERS);
        }
        try {
            ThumbnailVo thumbnailVo =  thumbnailService.upload(file, minIOPropertyConfig.getBucketName());
            String url = thumbnailVo.getUrl();
            thumbnailVo.setUrl(minIOPropertyConfig.getEndPoint() + url);
            return ResultUtil.successResult(ResultEnum.SUCCESS_STATUS, thumbnailVo);
        } catch (Exception e) {
            log.error("【缩略图】上传异常，异常信息：", e);
            return ResultUtil.errorResult(ExceptionEnum.UNKNOWN_EXCEPTION);
        }
    }
}
