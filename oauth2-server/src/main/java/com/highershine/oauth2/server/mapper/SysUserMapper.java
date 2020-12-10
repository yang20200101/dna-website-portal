package com.highershine.oauth2.server.mapper;


import com.highershine.oauth2.server.entity.SysUser;
import org.springframework.stereotype.Repository;

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
}
