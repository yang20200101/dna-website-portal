package com.highershine.portal.common.entity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel("活动报名信息Vo类")
public class ActivityUserVo {
    @ApiModelProperty(value = "参与人id", example = "1")
    private Long userId;

    @ApiModelProperty(value = "参与人姓名", example = "张三")
    private String nickname;

    @ApiModelProperty(value = "省份名称", example = "陕西省")
    private String province;

    @ApiModelProperty(value = "报名时间", example = "2018-11-04 11:21")
    private String createdAtStr;

    @ApiModelProperty(value = "最新标识", example = "true")
    private Boolean isLatest;

    @ApiModelProperty(value = "附件id", example = "130000")
    private Long thumbnailId;

    @ApiModelProperty(value = "非最新报名信息", example = "130000")
    private List<ActivityUserVo> userList;
}
