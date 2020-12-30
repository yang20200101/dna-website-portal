package com.highershine.portal.config;

import com.highershine.portal.filter.JWTAuthenticationFilter;
import com.highershine.portal.handlder.ApiAccessDeniedHandler;
import com.highershine.portal.handlder.AuthExceptionEntryPoint;
import com.highershine.portal.handlder.CustomerResponseErrorHandler;
import com.highershine.portal.handlder.LoginExpireHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.core.GrantedAuthorityDefaults;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.RemoteTokenServices;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter;
import org.springframework.web.client.RestTemplate;

@Configuration
@EnableResourceServer
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {

    @Value("${oauth2.server.checkTokenAddr}")
    private String checkTokenAddr;
    @Value("${oauth2.server.clientId}")
    private String clientId;
    @Value("${oauth2.server.clientSecret}")
    private String clientSecret;
    @Value("${oauth2.server.jwtSecret}")
    private String jwtSecret;

    //放行接口
    public static String[] passUrl =  {"/su/token/**",
                                "/su/refreshToken",
                                "/thumbnail/download/**",
                                "/personQuery/query/idCardNo",
                                "/region/getTree",
                                "/su/register",
                                "/app/status",
                                "/sd/getDictList/**",
                                "/version",
                                "/article/getList",
                                "/article/find/**",
                                "/advertisement/list",
                                "/category/getCategoryList",
                                "/category/find/**",
                                "/advertisement/list",
                                "/_health",
                                "/v2/api-docs", "/swagger-resources/configuration/ui","/swagger-resources", "/swagger-resources/configuration/security",
                                "/swagger-ui.html","/css/**", "/js/**","/images/**", "/webjars/**", "**/favicon.ico"};

    @Bean
    @LoadBalanced
    public RestTemplate restTemplate(RestTemplateBuilder builder){
        RestTemplate restTemplate = builder.build();
        /*为RestTemplate配置异常处理器0*/
        restTemplate.setErrorHandler(new CustomerResponseErrorHandler());
        return restTemplate;
    }

    //不使用权限校验的ROLE_前缀  (http.servletApi().rolePrefix("");该方式无效果，使用@Bean方式)
    @Bean
    GrantedAuthorityDefaults grantedAuthorityDefaults() {
        return new GrantedAuthorityDefaults(""); // Remove the ROLE_ prefix
    }

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
        resources.resourceId(clientId);

        resources.tokenStore(new JwtTokenStore(accessTokenConverter())).stateless(true);

        /* 配置令牌验证 */
        RemoteTokenServices remoteTokenServices = new RemoteTokenServices();
        remoteTokenServices.setAccessTokenConverter(accessTokenConverter());
        remoteTokenServices.setRestTemplate(restTemplate);
        remoteTokenServices.setCheckTokenEndpointUrl(checkTokenAddr);
        remoteTokenServices.setClientId(clientId);
        remoteTokenServices.setClientSecret(clientSecret);

        resources.tokenServices(remoteTokenServices).stateless(true);
        //check_token异常类
        resources.authenticationEntryPoint(new AuthExceptionEntryPoint());
    }

    @Bean
    public JwtAccessTokenConverter accessTokenConverter() {
        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
        converter.setSigningKey(jwtSecret);
        return converter;
    }

    /* 配置资源拦截规则 */
    @Override
    public void configure(HttpSecurity http) throws Exception {
        //关闭csrf
        http.cors().and().csrf().disable();

        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED);

        //放行接口配置
        http.authorizeRequests()
                .antMatchers(HttpMethod.OPTIONS).permitAll() // 对option不校验
                .antMatchers(passUrl).permitAll()
                .anyRequest().authenticated();

        //jwt校验
        http.addFilterBefore(new JWTAuthenticationFilter(), AbstractPreAuthenticatedProcessingFilter.class);
        //登录超时未登录处理
        http.exceptionHandling().authenticationEntryPoint(new LoginExpireHandler());
        //权限不足处理器 配合注解使用@Secured("admin")
        http.exceptionHandling().accessDeniedHandler(new ApiAccessDeniedHandler());
    }
}