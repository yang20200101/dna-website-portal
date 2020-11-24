package com.highershine.portal.common.entity.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * @Author zxk
 * @Date 2020/11/12 15:20
 * @Description TODO
 */
@Data
@ApiModel("飘窗DTO类")
public class AdvertisementDTO extends BaseDTO {

    @ApiModelProperty(value = "主键", name = "id", example = "1", dataType = "long")
    private Long id;
    @ApiModelProperty(value = "外部关联ID", name = "extId", example = "6fQDyTuEvH", dataType = "string")
    private String extId;
    @ApiModelProperty(value = "标题", dataType = "string")
    @NotNull(message = "标题为空")
    private String title;
    @ApiModelProperty(value = "点击跳转的活动链接", dataType = "string")
    @NotNull(message = "飘窗链接为空")
    private String link;
    @ApiModelProperty(value = "缩略图ID", dataType = "string")
    @NotNull(message = "飘窗缩略图地址为空")
    private String thumbnailId;
    @ApiModelProperty(value = "飘窗位置", dataType = "string")
    @NotNull(message = "飘窗位置为空")
    private String position;
    @ApiModelProperty(value = "飘窗等级", dataType = "string")
    private String level;
    @NotNull(message = "是否发布为空")
    @ApiModelProperty(value = "是否发布", dataType = "string")
    private Boolean isPublish;
    @ApiModelProperty(value = "创建时间", dataType = "date")
    private Date createdAt;
    @ApiModelProperty(value = "更新时间", dataType = "string")
    private Date updatedAt;
    @ApiModelProperty(value = "删除标识", dataType = "string")
    private Boolean deleted;
}
