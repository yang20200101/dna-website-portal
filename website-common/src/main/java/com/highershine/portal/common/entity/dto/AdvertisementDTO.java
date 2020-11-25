package com.highershine.portal.common.entity.dto;

import com.highershine.portal.common.entity.po.Thumbnail;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

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
    @ApiModelProperty(value = "标题", dataType = "string")
    @NotNull(message = "标题为空")
    private String title;
    @ApiModelProperty(value = "点击跳转的活动链接", dataType = "string")
    @NotNull(message = "飘窗链接为空")
    private String link;
    @ApiModelProperty(value = "缩略图")
    private Thumbnail thumbnail;
    @ApiModelProperty(value = "飘窗位置", dataType = "string")
    @NotNull(message = "飘窗位置为空")
    private String position;
    @ApiModelProperty(value = "飘窗等级", dataType = "string")
    private Integer level;
    @NotNull(message = "是否发布为空")
    @ApiModelProperty(value = "是否发布", dataType = "bool")
    private Boolean isPublish;
}
