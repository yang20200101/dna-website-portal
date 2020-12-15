package com.highershine.oauth2.server.service;

import org.springframework.security.core.Authentication;

import javax.servlet.http.HttpServletRequest;

/**
 * @Description:
 * @Author: mizhanlei
 * @Date: 2020/11/30 22:13
 */
public interface PermissionService {

    boolean hasPermission(HttpServletRequest request, Authentication authentication);
}
