package com.highershine.portal.common.mapper;


import com.highershine.portal.common.entity.po.SysUser;
import com.highershine.portal.common.entity.vo.ActivityPlayerVo;
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
     * @param username
     * @return
     */
    SysUser selectByUsername(String username);

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
}
