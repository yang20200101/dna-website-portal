package com.highershine.portal.common.entity.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Description
 * @Author zxk
 * @Date 2020/4/16 15:34
 **/
@ApiModel("活动DTO对象")
@Data
public class ActivityDTO extends BaseDTO{

    @ApiModelProperty(value = "标题", name = "title", example = "研讨会报名", dataType = "string")
    private String title;

}
