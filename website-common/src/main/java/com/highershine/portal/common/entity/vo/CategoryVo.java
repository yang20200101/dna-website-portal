package com.highershine.portal.common.entity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @Description 栏目Vo类
 * @Author zxk
 * @Date 2020/4/15 11:05
 **/
@Data
@ApiModel("栏目VO类")
public class CategoryVo {

    //主键
    @ApiModelProperty(value = "主键ID", name = "id", example = "1", dataType = "long")
    private Long id;

    //栏目名称
    @ApiModelProperty(value = "栏目名称", name = "name", example = "首页", dataType = "string")
    private String name;

    //别名
    @ApiModelProperty(value = "别名", name = "alias", example = "SY", dataType = "string")
    private String alias;

    //排序
    @ApiModelProperty(value = "排序", name = "sort", example = "1", dataType = "int")
    private Integer sort;

    //备注
    @ApiModelProperty(value = "备注", name = "remark", example = "备注", dataType = "string")
    private String remark;

    //删除状态  true  删除  默认false
    @ApiModelProperty(value = "删除状态，默认false", name = "deleted", example = "true", dataType = "bool")
    private Boolean deleted;

    //状态
    @ApiModelProperty(value = "状态", name = "status", example = "true", dataType = "bool")
    private Boolean status;

    //创建时间
    @ApiModelProperty(value = "创建时间", name = "createdAt", dataType = "date")
    private Date createdAt;

    //更新时间
    @ApiModelProperty(value = "更新时间", name = "updatedAt", dataType = "date")
    private Date updatedAt;
}
