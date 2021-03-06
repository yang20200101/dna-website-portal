package com.highershine.portal.common.entity.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.highershine.portal.common.entity.po.Thumbnail;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
@ApiModel("飘窗Vo类")
public class AdvertisementVo {

    @ApiModelProperty(value = "主键", name = "id", example = "1", dataType = "long")
    private Long id;
    @ApiModelProperty(value = "标题", dataType = "string")
    private String title;
    @ApiModelProperty(value = "点击跳转的活动链接", dataType = "string")
    private String link;
    @ApiModelProperty(value = "缩略图")
    private Thumbnail thumbnail;
    @ApiModelProperty(value = "缩略图id", dataType = "long")
    private Long thumbnailId;
    @ApiModelProperty(value = "飘窗位置", dataType = "string")
    private String position;
    @ApiModelProperty(value = "飘窗等级", dataType = "string")
    private Integer level;
    @ApiModelProperty(value = "是否发布", dataType = "string")
    private Boolean isPublish;
    @ApiModelProperty(value = "创建时间", dataType = "date")
    private Date createdAt;
    @ApiModelProperty(value = "更新时间", dataType = "string")
    private Date updatedAt;
    @ApiModelProperty(value = "删除标识", dataType = "string")
    private Boolean deleted;
    @JsonIgnore
    private String url;

}