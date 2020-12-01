package com.highershine.portal.common.entity.po;

import java.io.Serializable;
import java.util.Date;

/**
 * @Description: 用户实体类
 * @Author: mizhanlei
 * @Date: 2019/11/26 8:46
 */
public class SysRole implements Serializable {

    // 主键id
    private Long id;

    // 子模块标识
    private String subSystem;

    // 角色名
    private String roleName;

    // 角色描述
    private String roleDesc;

    // 启用标识
    private Boolean activeFlag;

    // 角色排序
    private int roleOrd;

    // 创建时间
    private Date createDatetime;

    // 创建人
    private String createUser;

    // 更新时间
    private Date updateDatetime;

    // 更新人
    private String updateUser;

    // 外部关联系统id
    private String extId;
}
