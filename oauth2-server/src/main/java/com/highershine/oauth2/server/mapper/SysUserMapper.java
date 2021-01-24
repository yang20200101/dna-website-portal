package com.highershine.oauth2.server.mapper;


import com.highershine.oauth2.server.entity.SysClientRole;
import com.highershine.oauth2.server.entity.SysUser;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Description
 * @Author xueboren
 * @Date 2020/11/27 9:48
 **/
@Repository
public interface SysUserMapper {
    /**
     * 根据用户名查询
     * @param username
     * @return
     */
    SysUser selectByUsername(String username);

    /**
     * 根据身份证号查询
     * @param idCardNo
     * @return
     */
    SysUser selectByIdCardNo(String idCardNo);

    /**
     * 根据userId查询系统角色
     * @param id
     * @return
     */
    List<SysClientRole> selectClientRoleByUserId(Long id);
}
