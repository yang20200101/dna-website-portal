package com.highershine.portal.common.entity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @Description: SysDictVo实体类
 * @Author: mizhanlei
 * @Date: 2019/11/29 14:55
 */
@Data
@ApiModel("字典展示对象")
public class SysDictVo implements Serializable {
    //主键ID
    @ApiModelProperty(value = "字典对象id", example = "2", dataType = "long")
    private Long id;

    //字典键
    @ApiModelProperty(value = "字典对象字典键", example = "1")
    private String dictKey;

    //字典值
    @ApiModelProperty(value = "字典对象字典值", example = "男")
    private String dictValue1;

    //排序字段
    @ApiModelProperty(value = "字典对象排序", example = "1", dataType = "int")
    private Long ord;
}
