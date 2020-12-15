package com.highershine.portal.config;

import com.highershine.portal.filter.JWTAuthenticationFilter;
import com.highershine.portal.handlder.AuthExceptionEntryPoint;
import com.highershine.portal.handlder.CustomerResponseErrorHandler;
import com.highershine.portal.handlder.LoginExpireHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.error.OAuth2AccessDeniedHandler;
import org.springframework.security.oauth2.provider.token.RemoteTokenServices;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter;
import org.springframework.web.client.RestTemplate;

@Configuration
@EnableResourceServer
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {


    @Bean
    @LoadBalanced
    public RestTemplate restTemplate(RestTemplateBuilder builder){
        RestTemplate restTemplate = builder.build();
        /*为RestTemplate配置异常处理器0*/
        restTemplate.setErrorHandler(new CustomerResponseErrorHandler());
        return restTemplate;
    }

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
        resources.resourceId("website");

        resources.tokenStore(new JwtTokenStore(accessTokenConverter())).stateless(true);

        /* 配置令牌验证 */
        RemoteTokenServices remoteTokenServices = new RemoteTokenServices();
        remoteTokenServices.setAccessTokenConverter(accessTokenConverter());
        remoteTokenServices.setRestTemplate(restTemplate);
        remoteTokenServices.setCheckTokenEndpointUrl("http://127.0.0.1:8080/oauth/check_token");
        remoteTokenServices.setClientId("website");
        remoteTokenServices.setClientSecret("2020");

        resources.tokenServices(remoteTokenServices).stateless(true);
        //check_token异常类
        resources.authenticationEntryPoint(new AuthExceptionEntryPoint());
    }

    @Bean
    public JwtAccessTokenConverter accessTokenConverter() {
        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
        converter.setSigningKey("highershine-jwt-key");
        return converter;
    }

    /* 配置资源拦截规则 */
    @Override
    public void configure(HttpSecurity http) throws Exception {
        //关闭csrf
        http.cors().and().csrf().disable();

        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED);

        http.authorizeRequests()
                .antMatchers(HttpMethod.OPTIONS).permitAll() // 对option不校验
                .antMatchers(
                        "/su/token/**",
                        "/su/userInfo",
                        "/region/getTree",
                        "/app/status",
                        "/sd/getDictList/**",
                        "/version",
                        "/article/getList",
                        "/article/find/**",
                        "/advertisement/list",
//                        "/category/getCategoryList",
                        "/category/find/**",
                        "/advertisement/list",
                        "/_health",
                        "/v2/api-docs", "/swagger-resources/configuration/ui","/swagger-resources", "/swagger-resources/configuration/security",
                        "/swagger-ui.html","/css/**", "/js/**","/images/**", "/webjars/**", "**/favicon.ico").permitAll()
                .anyRequest().authenticated();

        //jwt校验
        http.addFilterBefore(new JWTAuthenticationFilter(), AbstractPreAuthenticatedProcessingFilter.class);
        //登录超时未登录处理
        http.exceptionHandling().accessDeniedHandler(new OAuth2AccessDeniedHandler());
        http.exceptionHandling().authenticationEntryPoint(new LoginExpireHandler());
    }
}