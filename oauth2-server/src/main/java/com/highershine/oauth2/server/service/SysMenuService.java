package com.highershine.oauth2.server.service;

import java.util.List;

public interface SysMenuService {
   String[] selectUserMenu(List<String> roles);
}
