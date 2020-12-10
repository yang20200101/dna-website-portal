package com.highershine.oauth2.server.handlder;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.highershine.oauth2.server.entity.Jwt;
import com.highershine.oauth2.server.entity.Result;
import com.highershine.oauth2.server.entity.SysUser;
import com.highershine.oauth2.server.enums.ExceptionEnum;
import com.highershine.oauth2.server.enums.ResultEnum;
import com.highershine.oauth2.server.utils.JwtUtils;
import com.highershine.oauth2.server.utils.ResultUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @Description: TODO
 * @Author: mizhanlei
 * @Date: 2020/11/30 21:48
 */
@Slf4j
public class HHAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    public final ObjectMapper MAPPER = new ObjectMapper();

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        SysUser user = (SysUser) authentication.getPrincipal();
        Result result = null;
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        boolean loginBoolean = true;

        user.setPassword(null);
        long now = System.currentTimeMillis();

        JSONObject payload = new JSONObject();
        payload.put("iss","sys"); //签发人
        payload.put("aud",user.getUsername()); //受众
        payload.put("exp",now + JwtUtils.EXPIRE_TIME); //过期时间
        payload.put("nbf",now); //生效时间
        payload.put("iat",now); //签发时间
        payload.put("jti", user.getId()); //编号
        payload.put("sub","JWT-TEST"); //主题
        payload.put("user",user); //用户对象

        try {
            response.setHeader(JwtUtils.HEADER_TOKEN_NAME, new Jwt(payload.toJSONString()).toString() );
        } catch (Exception e) {
            loginBoolean = false;
        }
        if (loginBoolean){
            result = ResultUtil.successResult(ResultEnum.SUCCESS_STATUS);
            log.info("【登录】用户名：{}登录成功", user.getUsername());
            // 密码保护， 输出null
            log.info("【登录】密码：{}", user.getPassword());
            log.info("【登录】权限：{}", user.getAuthorities());

        }else{
            result = ResultUtil.errorResult(ExceptionEnum.UNKNOWN_EXCEPTION);
        }
        response.getWriter().write(MAPPER.writeValueAsString(result));
    }
}
