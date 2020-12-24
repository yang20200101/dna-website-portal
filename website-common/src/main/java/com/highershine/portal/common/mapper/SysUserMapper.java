package com.highershine.portal.common.mapper;


import com.highershine.portal.common.entity.dto.SysUserDTO;
import com.highershine.portal.common.entity.dto.SysUserListDTO;
import com.highershine.portal.common.entity.po.SysUser;
import com.highershine.portal.common.entity.vo.ActivityPlayerVo;
import com.highershine.portal.common.entity.vo.SysUserListVo;
import org.apache.ibatis.annotations.Param;
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
     * 根据ID查询
     * @param id
     * @return
     */
    SysUser selectByPrimaryKey(Long id);

    /**
     * 新增
     * @param record
     * @return
     */
    int insert(SysUser record);

    /**
     * 查询活动参与人
     * @return
     */
    List<ActivityPlayerVo> getActivityPlayerList();

    /**
     * 根据用户名查询
     * @param dto
     * @return
     */
    SysUser selectByUsername(SysUserDTO dto);

    /**
     * 查询oauth回调地址
     * @return
     */
    String selectOauthRedirectUri(String clientId);

    /**
     * 重置密码
     * @param id
     * @param password
     */
    void updatePassword(@Param("id") Long id, @Param("password")String password);

    /**
     * 根据身份证号查询
     * @param dto
     * @return
     */
    SysUser selectByIdCardNo(SysUserDTO dto);

    /**
     * 根据单位编码和工作岗位查询
     * @param dto
     * @return
     */
    int selectByOrgCodeJob(SysUserDTO dto);

    /**
     * 查询用户列表
     * @param dto
     * @return
     */
    List<SysUserListVo> selectListByDto(SysUserListDTO dto);

    /**
     * 更新ext_id
     * @param id
     */
    int updateExtId(Long id);

    /**
     * 用户删除
     * @param id
     * @return
     */
    int deleteUserById(Long id);

    /**
     * 修改用户
     * @param sysUser
     */
    void updateByPrimaryKey(SysUser sysUser);
}
