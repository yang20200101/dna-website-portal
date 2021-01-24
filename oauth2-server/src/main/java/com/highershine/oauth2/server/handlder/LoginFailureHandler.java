package com.highershine.oauth2.server.handlder;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.highershine.oauth2.server.constants.LoginConstant;
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
        if (e.getMessage().startsWith(LoginConstant.PASSWORD_EXCEPTION_PREFIX)) {
            //自定义输出异常
            result = ResultUtil.errorResult(ExceptionEnum.UNKNOWN_EXCEPTION.getCode(),
                    e.getMessage().replace(LoginConstant.PASSWORD_EXCEPTION_PREFIX, ""));
            String username = request.getParameter("username");
            log.error("【password登录】失败，用户名：{}", username);
        } else if (e.getMessage().startsWith(LoginConstant.PKI_NOT_FOUND_EXCEPTION_PREFIX)) {
            result = ResultUtil.errorResult(ExceptionEnum.NOT_FIND_DATA.getCode(),
                    e.getMessage().replace(LoginConstant.PKI_NOT_FOUND_EXCEPTION_PREFIX, ""));
            String idCardNo = request.getParameter("idCardNo");
            log.error("【pki登录】失败，身份证号：{}", idCardNo);
        }
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        response.getWriter().write(MAPPER.writeValueAsString(result));
        log.error("【登录失败】");
    }
}
