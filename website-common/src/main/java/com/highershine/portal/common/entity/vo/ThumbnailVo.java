package com.highershine.portal.common.entity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @Description
 * @Author zxk
 * @Date 2020/4/20 15:20
 **/
@Data
@ApiModel("缩略图VO类")
public class ThumbnailVo {

    @ApiModelProperty(value = "主键", name = "id", example = "1", dataType = "long")
    private Long id;
    @ApiModelProperty(value = "桶名", name = "bucketName", example = "test", dataType = "string")
    private String bucketName;
    @ApiModelProperty(value = "文件名", name = "fileName", example = "1", dataType = "string")
    private String fileName;
    @ApiModelProperty(value = "路径", name = "url", example = "1", dataType = "string")
    private String url;
    @ApiModelProperty(value = "删除状态", name = "deleted", example = "true", dataType = "bool")
    private Boolean deleted;
    @ApiModelProperty(value = "创建日期", name = "createdAt",  dataType = "date")
    private Date createdAt;
    @ApiModelProperty(value = "更新日期", name = "updatedAt", dataType = "date")
    private Date updatedAt;
}
