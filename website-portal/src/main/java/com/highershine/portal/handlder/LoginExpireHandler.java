package com.highershine.portal.handlder;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.highershine.portal.common.enums.HttpStatusEnum;
import com.highershine.portal.common.result.Result;
import com.highershine.portal.common.utils.ResultUtil;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class LoginExpireHandler implements AuthenticationEntryPoint {

    public final ObjectMapper MAPPER = new ObjectMapper();

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) throws IOException, ServletException {
        log.error("Spring Securtiy异常", e);
        response.setStatus(HttpStatusEnum.UNAUTHORIZED.getCode());
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        Result result = ResultUtil.errorResult(HttpStatusEnum.UNAUTHORIZED.getCode(),
                "登录过期或未登录");
        response.getWriter().write(MAPPER.writeValueAsString(result));
    }
}
