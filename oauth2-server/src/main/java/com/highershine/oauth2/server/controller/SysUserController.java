package com.highershine.oauth2.server.controller;

import io.jsonwebtoken.Jwts;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;

/**
 * @Description: TODO
 * @Author: mizhanlei
 * @Date: 2020/12/1 7:56
 */
@RestController
@RequestMapping("/user")
public class SysUserController {

    @RequestMapping("/userInfo")
    public Object getUserInfo(Authentication authentication, HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        String token = header.substring(header.lastIndexOf("bearer") + 7);
        return Jwts
                .parser()
                .setSigningKey("highershine-jwt-key".getBytes(StandardCharsets.UTF_8))
                .parseClaimsJws(token)
                .getBody();
    }
}
