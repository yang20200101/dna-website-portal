package com.highershine.portal.common.converter;

import com.highershine.portal.common.constants.RoleConstant;
import com.highershine.portal.common.entity.bo.ClientRoleBo;
import com.highershine.portal.common.entity.dto.SysUserDTO;
import com.highershine.portal.common.entity.po.SysUser;
import com.highershine.portal.common.entity.po.SysUserRole;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Description: 用户实体转换器
 * @Author: xueboren
 * @Date: 2020/12/24 14:03
 */
public final class SysUserConverter {
    private SysUserConverter() {
    }

    /**
     * 用户实体转换
     * @param dto
     * @return
     */
    public static SysUser transferSysUserDto2Po(SysUserDTO dto) {
        SysUser sysUser = new SysUser();
        BeanUtils.copyProperties(dto, sysUser);
        //管辖地区
        String serverNos = sysUser.getProvince();
        String serverNo = sysUser.getProvince();
        if (StringUtils.isNotBlank(sysUser.getArea())) {
            serverNos += "," + sysUser.getCity() + "," + sysUser.getArea();
            serverNo = sysUser.getArea();
        } else if (StringUtils.isNotBlank(sysUser.getCity())) {
            serverNos += "," + sysUser.getCity();
            serverNo = sysUser.getCity();
        }
        dto.setServerNo(serverNo);
        sysUser.setServerNos(serverNos);
        //是否新增实验室
        if (sysUser.getIsAddOrg() == null) {
            sysUser.setIsAddOrg(false);
        }
        //是否启用
        if (sysUser.getStatus() == null) {
            sysUser.setStatus(true);
        }
        sysUser.setDeleteFlag(false).setCreateDatetime(new Date()).setUpdateDatetime(new Date());
        return sysUser;
    }


    public static List<SysUserRole> transferSysUserDto2UserRole(SysUserDTO dto, String clientId) {
        //门户系统用户角色初始化
        if (CollectionUtils.isEmpty(dto.getClientRoles())) {
            ArrayList<ClientRoleBo> clientRoles = new ArrayList<>();
            ClientRoleBo clientRoleBo = new ClientRoleBo();
            //门户系统 普通用户
            clientRoleBo.setClientId(clientId);
            List<String> roleList = new ArrayList<>(1);
            roleList.add(RoleConstant.ROLE_AVERAGE);
            clientRoleBo.setRoles(roleList);
            clientRoles.add(clientRoleBo);
            dto.setClientRoles(clientRoles);
        }
        //构造SysUserRole对象
        String roles = "";
        List<SysUserRole> sysUserRoleList = new ArrayList<>();
        for (ClientRoleBo clientRole : dto.getClientRoles()) {
            String client = clientRole.getClientId();
            for (String role : clientRole.getRoles()) {
                SysUserRole sysUserRole = new SysUserRole();
                sysUserRole.setClientId(client).setRoleId(role).setUserId(dto.getId());
                if (client.equals(clientId)) {
                    roles += RoleConstant.getRoleExtId(role) + ",";
                }
                sysUserRoleList.add(sysUserRole);
            }
        }
        dto.setRoles(roles);
        return sysUserRoleList;
    }
}
