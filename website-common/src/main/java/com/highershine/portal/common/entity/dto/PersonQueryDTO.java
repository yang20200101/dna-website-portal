package com.highershine.portal.common.entity.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @Description
 * @Author zxk
 * @Date 2020/4/8 15:46
 **/
@Data
@ApiModel("户籍信息查询DTO")
public class PersonQueryDTO {

    @ApiModelProperty(
            value = "工作岗位",
            name = "idCardNo",
            example = "120221200201010413",
            required = true)
    @NotNull(message = "身份证号为空")
    private String idCardNo;
}
