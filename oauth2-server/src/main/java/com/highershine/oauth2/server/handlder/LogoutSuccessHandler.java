package com.highershine.oauth2.server.handlder;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.highershine.oauth2.server.entity.Result;
import com.highershine.oauth2.server.enums.ResultEnum;
import com.highershine.oauth2.server.utils.ResultUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.provider.token.TokenStore;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @Description: 登出成功处理
 * @Author: xueboren
 * @Date: 2020/12/14 13:31
 */
@Slf4j
public class LogoutSuccessHandler implements org.springframework.security.web.authentication.logout.LogoutSuccessHandler {

    public final ObjectMapper MAPPER = new ObjectMapper();

    private TokenStore tokenStore;

    public LogoutSuccessHandler(TokenStore tokenStore) {
        this.tokenStore = tokenStore;
    }

    @Override
    public void onLogoutSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {
        try {
            //TODO 清除redis中的token

            //返回退出成功信息
            httpServletResponse.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
            Result result = ResultUtil.successResult(ResultEnum.SUCCESS_STATUS);
            PrintWriter out = httpServletResponse.getWriter();
            out.write(MAPPER.writeValueAsString(result));
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
