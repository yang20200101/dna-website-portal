package com.highershine.portal.common.entity.po;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

@Data
@Accessors(chain = true)
@ApiModel("附件")
public class Thumbnail {
    @ApiModelProperty(value = "附件id", example = "112")
    private Long id;

    @ApiModelProperty(hidden = true)
    private String bucketName;

    @ApiModelProperty(value = "文件名称", example = "涨涨工资吧.jpg")
    private String fileName;

    @ApiModelProperty(value = "附件url", example = "112")
    private String url;

    @ApiModelProperty(hidden = true)
    private Boolean deleted;

    @ApiModelProperty(hidden = true)
    private Date createdAt;

    @ApiModelProperty(hidden = true)
    private Date updatedAt;

}
