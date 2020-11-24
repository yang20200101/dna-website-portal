package com.highershine.portal.common.entity.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @Description
 * @Author zxk
 * @Date 2020/4/21 15:42
 **/
@Data
@ApiModel("发布DTO对象")
public class BatchPublishDTO {

    @ApiModelProperty(value = "文章草稿ID集合", name = "idList")
    private List<String> idList;
}
