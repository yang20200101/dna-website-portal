package com.highershine.oauth2.server.handlder;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.highershine.oauth2.server.entity.Result;
import com.highershine.oauth2.server.enums.HttpStatusEnum;
import com.highershine.oauth2.server.utils.ResultUtil;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @Description: 登录超时 未登录处理器
 * @Author: xueboren
 * @Date: 2020/12/09 13:41
 */
public class LoginExpireHandler implements AuthenticationEntryPoint {

    public final ObjectMapper MAPPER = new ObjectMapper();

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) throws IOException, ServletException {
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        Result result = ResultUtil.errorResult(HttpStatusEnum.UNAUTHORIZED.getCode(),
                "登录过期或未登录");
        response.getWriter().write(MAPPER.writeValueAsString(result));
    }
}
