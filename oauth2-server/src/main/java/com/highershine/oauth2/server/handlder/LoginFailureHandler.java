package com.highershine.oauth2.server.handlder;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.highershine.oauth2.server.entity.Result;
import com.highershine.oauth2.server.enums.ExceptionEnum;
import com.highershine.oauth2.server.utils.ResultUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 登录失败处理器
 */
@Slf4j
public class LoginFailureHandler implements AuthenticationFailureHandler {

    public final ObjectMapper MAPPER = new ObjectMapper();

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) throws IOException, ServletException {
        Result result = ResultUtil.errorResult(ExceptionEnum.UNKNOWN_EXCEPTION.getCode(),
                "登录失败");
        String username = request.getParameter("username");
        if (e.getMessage().contains("is not exists") || e.getMessage().contains("password error")) {
            result = ResultUtil.errorResult(ExceptionEnum.UNKNOWN_EXCEPTION.getCode(),
                    "用户或密码错误");
        }
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        response.getWriter().write(MAPPER.writeValueAsString(result));
        log.error("【登录失败】用户名：{}", username);
    }
}
