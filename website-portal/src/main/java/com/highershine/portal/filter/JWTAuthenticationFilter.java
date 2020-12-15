package com.highershine.portal.filter;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.*;
import java.io.IOException;

/**
 * jwt过滤器  配合redis校验
 */
@Slf4j
public class JWTAuthenticationFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        log.info("【JWT过滤器】start");

        //TODO 查看redis中，token是否过期


        filterChain.doFilter(request, response);
    }

}
