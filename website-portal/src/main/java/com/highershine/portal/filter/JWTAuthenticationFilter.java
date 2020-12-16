package com.highershine.portal.filter;

import com.highershine.portal.common.utils.JwtUtils;
import com.highershine.portal.config.ResourceServerConfig;
import lombok.extern.slf4j.Slf4j;

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

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        log.info("【JWT过滤器】start");
        //TODO 查看redis中，token是否过期
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        String uri = request.getRequestURI();
        log.info("uri_{}:{}, request.getContentType(): {}", request.getMethod(), uri, request.getContentType());
        String header = request.getHeader(JwtUtils.HEADER_TOKEN_NAME);
        if (!isNeedFilter(uri, ResourceServerConfig.passUrl)) {
            if (uri.contains("userInfo")) {
                log.error("isNeedFilter【{}】", header);
            }
            //如果是放行接口，构造出无token的request
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
        }
        if (uri.contains("userInfo")) {
            System.out.println("【jwtFilter header】"+header);
        }
        filterChain.doFilter(request, response);
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
