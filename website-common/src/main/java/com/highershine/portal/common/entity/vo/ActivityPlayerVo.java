package com.highershine.portal.common.entity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("参与者Vo类")
public class ActivityPlayerVo {
    @ApiModelProperty(value = "参与人id", example = "1")
    private Long id;

    @ApiModelProperty(value = "参与人姓名", example = "张三")
    private String nickname;
}
