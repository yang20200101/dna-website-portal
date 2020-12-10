package com.highershine.portal.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.highershine.portal.common.constants.CommonConstant;
import com.highershine.portal.common.constants.RedisConstant;
import com.highershine.portal.common.entity.bo.SysUserBo;
import com.highershine.portal.common.enums.ExceptionEnum;
import com.highershine.portal.common.utils.ResultUtil;
import com.highershine.portal.config.RequestWrapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author highershine.mizhanlei
 * @date 2019/12/2 16:11
 */
@Slf4j
@Component
@WebFilter(filterName = "sessionFilter", urlPatterns = {"/*"})
public class SessionFilter implements Filter {

    @Value("${manage.session.timeout}")
    private long timeout;

    private final ObjectMapper mapper = new ObjectMapper();

    // 不需要登录就可以访问的路径(比如:注册登录等)
    String[] includeUrls = new String[]{
            "/web-portal/su/login",
            "/web-portal/su/logout",
            "/su/login",
            "/web-portal/region/getTree",
            "region/getTree",
            "/web-portal/app/status",
            "/app/status",
            "/web-portal/sd/getDictList/",
            "/sd/getDictList/",
            "/su/delete",
            "/su/register/valid",
            "/version",
            "/article/getList",
            "/article/find",
            "/advertisement/list",
            "/categoryget/getCategoryList",
            "/categoryget/find",
            "/advertisement/list",
            "/_health"};

    // swagger 相关接口
    String[] includeUrlSwagger = new String[]{"/webjars", "/v2", "/swagger-resources", "/swagger-ui"};

    // 不需要登录就可以访问的路径(application/json请求)
    String[] decludeUrls = new String[]{
            "/web-portal/su/list",
            "/su/list",
            "/web-portal/soc/list",
            "/soc/list",
            "/web-portal/sysLabInfos/list/",
            "/sysLabInfos/list/",
            "/web-portal/sysLabInfos",
            "/sysLabInfos",
            "/web-portal/personQuery/query/idCardNo",
            "/personQuery/query/idCardNo"};

    /**
     * 登录校验
     *
     * @param servletRequest
     * @param servletResponse
     * @param filterChain
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse,
                         FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        RequestWrapper requestWrapper = generateRequestWrapper(request);
        // 设置跨域
        setAccessAllow(request, response);

        String uri = request.getRequestURI();
        String jsessionId = "";
        log.info("uri_{}:{}, request.getContentType(): {}", request.getMethod(), uri, request.getContentType());
        log.info("parameters:{}", request.getParameterNames());
        if (request.getMethod().equals("OPTIONS")) {
            response.setStatus(HttpStatus.OK.value());
            return;
        }

        //是否需要过滤
        boolean needFilter = isNeedFilter(uri, includeUrls);
        if (!needFilter) {
            //不需要过滤直接传给下一个过滤器
            filterChain.doFilter(requestWrapper != null ? requestWrapper : request, servletResponse);
        } else if (!isNeedFilter(uri, decludeUrls)) {
            filterChain.doFilter(requestWrapper != null ? requestWrapper : request, servletResponse);
        } else {
            Cookie[] cookies = request.getCookies();
            if (cookies == null || cookies.length == 0) {
                cookieNoJessionid(response);
            } else {
                for (Cookie cookie : cookies) {
                    if (CommonConstant.JSESSIONID.equals(cookie.getName())) {
                        jsessionId = cookie.getValue();
                        break;
                    }
                }
                judgeLogin(jsessionId, response, request, requestWrapper, filterChain);
            }
        }
    }


    /**
     * 判断是否已登录
     *
     * @param jsessionId
     * @param response
     * @param request
     * @param filterChain
     * @throws IOException
     * @throws ServletException
     */
    private void judgeLogin(String jsessionId, HttpServletResponse response, HttpServletRequest request,
                            RequestWrapper requestWrapper, FilterChain filterChain) throws IOException, ServletException {
        // 判断 jsessionId 是否为空
        if (StringUtils.isBlank(jsessionId)) {
            cookieNoJessionid(response);
        } else {
            // 获取ValueOperations bean
            ServletContext context = request.getServletContext();
            ApplicationContext ctx = WebApplicationContextUtils.getWebApplicationContext(context);
            ValueOperations valueOperations = (ValueOperations) ctx.getBean("valueOperations");

            // 获取用户信息
            String userBoStr = (String) valueOperations.get(RedisConstant.REDIS_LOGIN + jsessionId);
            if (StringUtils.isNotBlank(userBoStr) && mapper.readValue(userBoStr, SysUserBo.class) != null) {
                filterChain.doFilter(requestWrapper != null ? requestWrapper : request, response);
            } else {
                // 返回登录超时提醒
                response.setContentType(CommonConstant.APPLICATION_JSON);
                response.setCharacterEncoding("UTF-8");
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                response.getWriter().write(this.mapper.writeValueAsString(ResultUtil.errorResult(
                        ExceptionEnum.ERROR_PARAMETERS.getCode(), "jsessionid 登录超时")));
            }
        }
    }

    /**
     * cookie 无jsessionid
     *
     * @param response
     * @throws IOException
     */
    private void cookieNoJessionid(HttpServletResponse response) throws IOException {
        response.setContentType(CommonConstant.APPLICATION_JSON);
        response.setCharacterEncoding("UTF-8");
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.getWriter().write(this.mapper.writeValueAsString(ResultUtil.errorResult(
                ExceptionEnum.ERROR_PARAMETERS.getCode(),
                "jsessionid 为空！")));
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
            if (includeUrl.equals(uri) || uri.contains(includeUrl)) {
                isNeedFilter = false;
                break;
            }
        }

        for (String swaggerUrl: includeUrlSwagger) {
            if (uri.contains(swaggerUrl)) {
                isNeedFilter = false;
                break;
            }
        }

        return isNeedFilter;
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

    /**
     * 生成RequestWrapper
     *
     * @param request
     * @return
     */
    private RequestWrapper generateRequestWrapper(HttpServletRequest request) {
        RequestWrapper requestWrapper = null;
        if (request instanceof HttpServletRequest
                && "POST".equalsIgnoreCase(request.getMethod())
                && request.getContentType() != null
                && request.getContentType().contains(CommonConstant.APPLICATION_JSON)) {
            requestWrapper = new RequestWrapper(request);
        }
        return requestWrapper;
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // default implementation ignored
    }

    @Override
    public void destroy() {
        // default implementation ignored
    }
}
