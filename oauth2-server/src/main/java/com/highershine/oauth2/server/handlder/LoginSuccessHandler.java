package com.highershine.oauth2.server.handlder;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.highershine.oauth2.server.entity.Result;
import com.highershine.oauth2.server.entity.SysUser;
import com.highershine.oauth2.server.enums.ResultEnum;
import com.highershine.oauth2.server.utils.ResultUtil;
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
        Result result = null;
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        HashMap<Object, Object> map = new HashMap<>();
        List<String> list = new ArrayList<>();
        list.add("admin");
        map.put("roles", list);
        result = ResultUtil.successResult(ResultEnum.SUCCESS_STATUS, map);
        log.info("【登录】用户名：{}登录成功", user.getUsername());
        // 密码保护， 输出null
        log.info("【登录】密码：{}", user.getPassword());
        log.info("【登录】权限：{}", user.getAuthorities());
        response.getWriter().write(MAPPER.writeValueAsString(result));
    }
}
