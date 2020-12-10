package com.highershine.oauth2.server.config;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.highershine.oauth2.server.entity.SysUser;
import com.highershine.oauth2.server.utils.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public class JWTAuthenticationFilter extends GenericFilterBean {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;

        // 设置跨域
        setAccessAllow(req, resp);
        String uri = req.getRequestURI();
        log.info("uri_{}:{}, request.getContentType(): {}", req.getMethod(), uri, request.getContentType());
        log.info("parameters:{}", req.getParameterNames());
        if (req.getMethod().equals("OPTIONS")) {
            resp.setStatus(HttpStatus.OK.value());
            return;
        }
        String token = req.getHeader(JwtUtils.HEADER_TOKEN_NAME);
        /* token为null直接走登录的过滤器，不为空走下面 */
        if (token!=null&&token.trim().length()>0) {
            String tokenBody = null;
            try {
                tokenBody = JwtUtils.testJwt(token);
            } catch (Exception e) {
                e.printStackTrace();
            }
            /* 从token中取出用户信息，放在上下文中 */
            if (tokenBody!=null&&tokenBody.trim().length()>0){
                JSONObject user = JSON.parseObject(tokenBody).getJSONObject("user");
                SysUser sysUser = JSON.toJavaObject(user,SysUser.class);
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                        sysUser, null, sysUser.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            }else{
                HttpServletResponse res = (HttpServletResponse) response;
                res.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
                res.getWriter().write("{\"code\": \"405\", \"msg\": \"token错误\"}");
                return;
            }
        }
        filterChain.doFilter(request, response);
    }

    /**
     * 设置跨域
     *
     * @param request
     * @param response
     */
    private void setAccessAllow(HttpServletRequest request, HttpServletResponse response) {
        String origin = request.getHeader("Origin");
        if (origin == null) {
            origin = request.getHeader("Referer");
            if (origin == null) {
                origin = "*";
            }
        }
        response.setHeader("Access-Control-Allow-Origin", origin);
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setHeader("Access-Control-Allow-Methods", "*");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type,Access-Token,Authorization");
        response.setHeader("Access-Control-Expose-Headers", "*");
    }
}
