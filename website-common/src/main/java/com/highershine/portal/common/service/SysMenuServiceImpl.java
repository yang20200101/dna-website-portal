package com.highershine.portal.common.service;

import com.highershine.portal.common.mapper.SysMenuMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SysMenuServiceImpl implements SysMenuService {
    @Autowired
    private SysMenuMapper sysMenuMapper;

    @Override
    public String[] selectUserMenu(List<String> roles) {
        List<String> idList = new ArrayList<>();
        for (String roleName : roles) {
            idList.addAll(this.sysMenuMapper.selectMenuIdsByRoleId(roleName));
        }
        String[] menuIds = idList.stream().distinct().sorted().toArray(String[]::new);
        return menuIds;
    }
}
