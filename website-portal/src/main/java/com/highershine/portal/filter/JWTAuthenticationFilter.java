package com.highershine.portal.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.highershine.portal.common.constants.RedisConstant;
import com.highershine.portal.common.entity.vo.SysUserVo;
import com.highershine.portal.common.utils.ResultUtil;
import com.highershine.portal.common.utils.SysUserUtil;
import com.highershine.portal.config.ResourceServerConfig;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;

/**
 * jwt过滤器  配合redis校验
 */
@Slf4j
public class JWTAuthenticationFilter implements Filter {

    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        log.info("【JWT过滤器】start");
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        //如果是放行接口，构造出无token的request后直接放行（这样做的话不会调用/oauth/check_token接口进行校验）
        String uri = request.getRequestURI();
        log.info("uri_{}:{}, request.getContentType(): {}", request.getMethod(), uri, request.getContentType());
        if (!isNeedFilter(uri, ResourceServerConfig.passUrl)) {
            request = new HttpServletRequestWrapper(request) {
                private Set<String> headerNameSet;

                @Override
                public Enumeration<String> getHeaderNames() {
                    if (headerNameSet == null) {
                        // first time this method is called, cache the wrapped request's header names:
                        headerNameSet = new HashSet<>();
                        Enumeration<String> wrappedHeaderNames = super.getHeaderNames();
                        while (wrappedHeaderNames.hasMoreElements()) {
                            String headerName = wrappedHeaderNames.nextElement();
                            if (!"Authorization".equalsIgnoreCase(headerName)) {
                                headerNameSet.add(headerName);
                            }
                        }
                    }
                    return Collections.enumeration(headerNameSet);
                }

                @Override
                public Enumeration<String> getHeaders(String name) {
                    if ("Authorization".equalsIgnoreCase(name)) {
                        return Collections.<String>emptyEnumeration();
                    }
                    return super.getHeaders(name);
                }

                @Override
                public String getHeader(String name) {
                    if ("Authorization".equalsIgnoreCase(name)) {
                        return null;
                    }
                    return super.getHeader(name);
                }
            };
            filterChain.doFilter(request, response);
        } else {
            //非放行的接口
            String header = request.getHeader(HttpHeaders.AUTHORIZATION);
            //是否过期
            boolean expireFlag = false;
            try {
                if (StringUtils.isNotBlank(header)) {
                    //token串
                    String token = header.substring(header.lastIndexOf("bearer") + 8);
                    // 获取ValueOperations bean
                    ServletContext context = request.getServletContext();
                    ApplicationContext ctx = WebApplicationContextUtils.getWebApplicationContext(context);
                    ValueOperations valueOperations = (ValueOperations) ctx.getBean("valueOperations");
                    SysUserUtil sysUserUtil = (SysUserUtil) ctx.getBean("sysUserUtil");
                    SysUserVo user = sysUserUtil.getSysUserVoByToken(token);
                    String redisToken = (String) valueOperations.get(RedisConstant.REDIS_LOGIN + user.getUsername());
                    // token失效 或 token不正确
                    if (StringUtils.isBlank(redisToken) || !token.equals(redisToken)) {
                        expireFlag = true;
                    }
                } else {
                    // 没有token
                    expireFlag = true;
                }
            } catch (Exception e) {
                expireFlag = true;
                log.error("【jwtToken】校验出错，异常信息:{}", e);
            }
            if (expireFlag) {
                response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
                response.setCharacterEncoding("UTF-8");
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                response.getWriter().write(this.mapper.writeValueAsString(ResultUtil.errorResult(
                        HttpStatus.UNAUTHORIZED.value(), "token失效")));
            } else {
                filterChain.doFilter(request, response);
            }
        }
    }





    /**
     * @param uri
     * @Author: highershine.mizhanlei
     * @Description: 是否需要过滤
     * @Date: 2019-12-02
     */
    public boolean isNeedFilter(String uri, String[] includeUrls) {
        boolean isNeedFilter = true;
        for (String includeUrl : includeUrls) {
            includeUrl = includeUrl.replace("/**","");
            if (includeUrl.equals(uri) || uri.contains(includeUrl)) {
                isNeedFilter = false;
                break;
            }
        }
        return isNeedFilter;
    }
}
