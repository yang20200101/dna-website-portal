package com.highershine.portal.common.entity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
@ApiModel("活动Vo类")
public class ActivityListVo {
    @ApiModelProperty(value = "主键", name = "id", example = "1", dataType = "long")
    private Long id;

    @ApiModelProperty(value = "标题", name = "title", example = "研讨会报名", dataType = "string")
    private String title;

    @ApiModelProperty(value = "活动截至日期", name = "date", example = "2019-03-05", dataType = "date")
    private Date date;

    @ApiModelProperty(value = "报名人数", name = "applyNumber", example = "5", dataType = "int")
    private int applyNumber;

    @ApiModelProperty(value = "参与人数", name = "countNumber", example = "13", dataType = "int")
    private Integer countNumber;
}
