package com.highershine.portal.common.entity.vo;

import com.highershine.portal.common.entity.po.Thumbnail;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
@ApiModel("活动Vo类")
public class ActivityVo {
    @ApiModelProperty(value = "主键", example = "1")
    private Long id;

    @ApiModelProperty(value = "标题", example = "研讨会报名")
    private String title;

    @ApiModelProperty(value = "参与者")
    private List<ActivityPlayerVo> playerList;

    @ApiModelProperty(value = "活动截至日期", example = "2019-03-05")
    private Date date;

    @ApiModelProperty(value = "描述", example = "天气不错，约一波")
    private String description;

    @ApiModelProperty(value = "内容", example = "网吧五连坐")
    private String content;

    @ApiModelProperty(value = "缩略图")
    private Thumbnail thumbnail;
}
