package com.highershine.portal.common.mapper;

import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SysMenuMapper {
    List<String> selectMenuIdsByRoleId(String roleName);
}
