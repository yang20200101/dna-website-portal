package com.highershine.oauth2.server.handlder;

import com.highershine.oauth2.server.entity.SysUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @Description: TODO
 * @Author: mizhanlei
 * @Date: 2020/11/30 21:48
 */
@Slf4j
public class HHAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private String url;

    public HHAuthenticationSuccessHandler(String url) {
        this.url = url;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest,
                                        HttpServletResponse httpServletResponse,
                                        Authentication authentication) throws IOException, ServletException {
        SysUser user = (SysUser) authentication.getPrincipal();
        log.info("【登录】用户名：{}", user.getUsername());

        // 密码保护， 输出null
        log.info("【登录】密码：{}", user.getPassword());
        log.info("【登录】权限：{}", user.getAuthorities());
        httpServletResponse.sendRedirect(url);
    }
}
