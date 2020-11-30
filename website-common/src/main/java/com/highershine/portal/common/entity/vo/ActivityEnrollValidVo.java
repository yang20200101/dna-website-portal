package com.highershine.portal.common.entity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("活动报名校验Vo类")
public class ActivityEnrollValidVo {
    @ApiModelProperty(value = "true有权限,false无权限", example = "true")
    private boolean powerFlag;

    @ApiModelProperty(value = "true报名过,false未报名", example = "false")
    private boolean enrollFlag;
}
