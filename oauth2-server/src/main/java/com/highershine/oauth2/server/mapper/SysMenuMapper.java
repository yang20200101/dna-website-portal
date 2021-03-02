package com.highershine.oauth2.server.mapper;

import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SysMenuMapper {
    List<String> selectMenuIdsByRoleId(String roleName);
}
