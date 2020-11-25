package com.highershine.portal.common.entity.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @Description
 * @Author zxk
 * @Date 2020/4/15 16:14
 **/
@Data
@ApiModel("栏目DTO类")
public class CategoryDTO extends BaseDTO{
    //主键
    @ApiModelProperty(value = "主键ID", name = "id", example = "1", dataType = "long")
    private Long id;

    //栏目名称
    @ApiModelProperty(value = "栏目名称", name = "name", example = "首页", dataType = "string")
    @NotNull(message = "栏目名称为空")
    private String name;

    //别名
    @ApiModelProperty(value = "别名", name = "alias", example = "SY", dataType = "string")
    @NotNull(message = "栏目别名为空")
    private String alias;

    //排序
    @ApiModelProperty(value = "排序", name = "sort", example = "1", dataType = "int")
    @NotNull(message = "栏目排序为空")
    private Integer sort;

    //备注
    @ApiModelProperty(value = "备注", name = "remark", example = "备注", dataType = "string")
    private String remark;

    //删除状态  true  删除  默认false
    @ApiModelProperty(value = "删除状态，默认false", name = "deleted", example = "true", dataType = "bool")
    private Boolean deleted;

    //状态
    @ApiModelProperty(value = "状态", name = "status", example = "true", dataType = "bool")
    @NotNull(message = "状态为空")
    private Boolean status;
}
