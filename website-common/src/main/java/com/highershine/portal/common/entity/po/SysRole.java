package com.highershine.portal.common.entity.po;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * @Description: 角色实体类
 * @Author: xueboren
 * @Date: 2019/11/26 8:46
 */
@Data
@Accessors(chain = true)
public class SysRole implements Serializable {
    // 主键id
    private String id;

    // 主键id
    private String extId;

    // 角色名
    private String roleName;

    private String roleDesc;

    private Boolean activeFlag;

    private Integer roleOrd;

    private String createUser;

    private Date createDatetime;

    private String updateUser;

    private Date updateDatetime;
}
