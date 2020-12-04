package com.highershine.oauth2.server.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * @Description: TODO
 * @Author: mizhanlei
 * @Date: 2020/11/30 21:39
 */
@Controller
public class LoginController {

    @PostMapping("/toIndex")
    // @Secured("ROLE_admin")
    @PreAuthorize("hasRole('admin')")
    public String toIndex() {
        return "redirect:index.html";
    }
}
