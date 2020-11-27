package com.highershine.portal.common.entity.dto;

import com.highershine.portal.common.entity.po.Thumbnail;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

/**
 * @Description
 * @Author zxk
 * @Date 2020/4/16 15:34
 **/
@ApiModel("活动DTO对象")
@Data
public class ActivityDTO extends BaseDTO{
    @ApiModelProperty(value = "活动id（修改必填）", example = "10086", required = false)
    private Long id;

    @ApiModelProperty(value = "*标题", example = "周六嗨皮一下", required = true)
    @NotBlank(message = "标题为空")
    private String title;

    @ApiModelProperty(value = "*参与者id数组", example = "[1,3,5]", required = true)
    @NotNull(message = "参与者为空")
    private List<Long> userIdList;

    @ApiModelProperty(value = "缩略图", required = true)
    private Thumbnail thumbnail;

    @ApiModelProperty(value = "截止时间", example = "2019-02-01", required = true)
    private Date date;

    @ApiModelProperty(value = "描述", example = "天气很好", required = true)
    private String description;

    @ApiModelProperty(value = "活动内容", example = "网吧五连坐", required = true)
    private String content;
}
