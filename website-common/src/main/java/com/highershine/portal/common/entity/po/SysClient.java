package com.highershine.portal.common.entity.po;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * @Description: 系统清单表
 * @Author: xueboren
 * @Date: 2020/12/23 15:23
 */
@Data
@Accessors(chain = true)
public class SysClient implements Serializable {
    // 客户端id
    private String id;

    // 客户端名称
    private String clientName;

    // 系统角色接口地址
    private String roleUrl;

    // 排序
    private Integer sort;

    private Boolean deleteFlag;

    private Date createDatetime;

    private Date updateDatetime;
}
