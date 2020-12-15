package com.highershine.portal.handlder;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.highershine.portal.common.enums.HttpStatusEnum;
import com.highershine.portal.common.result.Result;
import com.highershine.portal.common.utils.ResultUtil;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.common.exceptions.InvalidTokenException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * check_token异常类
 */
@Component
public class AuthExceptionEntryPoint implements AuthenticationEntryPoint {


    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws ServletException {
        Map<String, Object> map = new HashMap<String, Object>();
        Throwable cause = authException.getCause();

        response.setStatus(HttpStatusEnum.UNAUTHORIZED.getCode());
        response.setHeader("Content-Type", "application/json;charset=UTF-8");
        try {
            if (cause instanceof InvalidTokenException) {
                Result result = ResultUtil.errorResult(HttpStatusEnum.UNAUTHORIZED.getCode(),
                        "token失效");
                response.getWriter().write(new ObjectMapper().writeValueAsString(result));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
