package com.highershine.oauth2.server.handlder;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.highershine.oauth2.server.entity.SysRole;
import com.highershine.oauth2.server.entity.SysUser;
import com.highershine.oauth2.server.enums.ResultEnum;
import com.highershine.oauth2.server.service.SysMenuService;
import com.highershine.oauth2.server.utils.ResultUtil;
import com.highershine.oauth2.server.utils.SpringBeanUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @Description: 登录成功处理
 * @Author: xueboren
 * @Date: 2020/11/30 21:48
 */
@Slf4j
public class LoginSuccessHandler implements AuthenticationSuccessHandler {

    public final ObjectMapper MAPPER = new ObjectMapper();

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        SysUser user = (SysUser) authentication.getPrincipal();
        log.info("【登录】用户名：{}登录成功", user.getUsername());
        log.info("【登录】权限：{}", user.getAuthorities());
        // 返回权限
        HashMap<Object, Object> map = new HashMap<>();
        List<SysRole> authorities = user.getAuthorities();
        List<String> roles = new ArrayList<>();
        for (SysRole role : authorities) {
            roles.add(role.getAuthority());
        }
        map.put("roles", roles);
        SysMenuService sysMenuService = (SysMenuService)SpringBeanUtil.getContext().getBean("sysMenuServiceImpl");
        map.put("userMenu",sysMenuService.selectUserMenu(roles));
        // 返回给前端
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        response.getWriter().write(MAPPER.writeValueAsString(ResultUtil.successResult(ResultEnum.SUCCESS_STATUS, map)));
    }
}
