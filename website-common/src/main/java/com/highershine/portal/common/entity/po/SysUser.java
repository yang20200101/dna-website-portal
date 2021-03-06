package com.highershine.portal.common.entity.po;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * @Description: 用户实体类
 * @Author: mizhanlei
 * @Date: 2019/11/26 8:46
 */
@Data
@Accessors(chain = true)
public class SysUser implements Serializable {
    private Long id;

    private String username;

    private String nickname;

    private String password;

    private String province;

    private String serverNos;

    private String idCardNo;

    private Date birthDate;

    private String gender;

    private String phone;

    private String orgCode;

    private String labName;

    private String remark;

    private String city;

    private Boolean isAddOrg;

    private String area;

    private String job;

    private String orgAddCode;

    private String orgAddName;

    private String labCode;

    private Boolean status;

    private Boolean deleteFlag;

    private Date createDatetime;

    private Date updateDatetime;
}
