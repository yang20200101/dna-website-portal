package com.highershine.portal.handlder;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.highershine.portal.common.enums.HttpStatusEnum;
import com.highershine.portal.common.result.Result;
import com.highershine.portal.common.utils.ResultUtil;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * 403 权限不足处理器
 */
public class ApiAccessDeniedHandler implements AccessDeniedHandler {

    public final ObjectMapper MAPPER = new ObjectMapper();

    @Override
    public void handle(HttpServletRequest httpServletRequest, HttpServletResponse response, AccessDeniedException e) throws IOException, ServletException {
        response.setStatus(HttpStatusEnum.FORBIDDEN.getCode());
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        Result result = ResultUtil.errorResult(HttpStatusEnum.FORBIDDEN.getCode(),
                "不允许访问");
        PrintWriter out = response.getWriter();
        out.write(MAPPER.writeValueAsString(result));
        out.flush();
        out.close();
    }
}
