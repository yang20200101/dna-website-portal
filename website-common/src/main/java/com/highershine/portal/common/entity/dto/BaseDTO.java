package com.highershine.portal.common.entity.dto;

import com.highershine.portal.common.constants.PageConstant;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author zxk
 * @Date 2020/11/12 14:49
 * @Description
 */
@Data
public class BaseDTO {

    //分页参数
    @ApiModelProperty(value = "当前页", name = "pageNum", example = "1", dataType = "int")
    private Integer current = PageConstant.PAGE_CURRENT;

    @ApiModelProperty(value = "每页显示条数", name = "pageSize", example = "10", dataType = "int")
    private Integer pageSize = PageConstant.PAGE_SIZE;
}
