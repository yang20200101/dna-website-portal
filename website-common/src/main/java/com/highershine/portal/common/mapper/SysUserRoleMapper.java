package com.highershine.portal.common.mapper;

import com.highershine.portal.common.entity.bo.ClientRoleBo;
import com.highershine.portal.common.entity.po.SysUserRole;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Description: 用户角色mapper
 * @Author: xueboren
 * @Date: 2019/12/22 10:14
 */
@Repository
public interface SysUserRoleMapper {

    /**
     * 批量插入
     * @param list
     */
    void batchInsert(List<SysUserRole> list);

    /**
     * 根据user_id删除
     * @param id
     * @return
     */
    int deleteByUserId(Long id);

    /**
     * 客户端角色信息
     * @param id
     * @return
     */
    List<ClientRoleBo> selectClientRoleByUserId(Long id);
}
