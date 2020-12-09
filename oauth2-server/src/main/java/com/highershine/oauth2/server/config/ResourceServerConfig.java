package com.highershine.oauth2.server.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.error.OAuth2AccessDeniedHandler;
import org.springframework.security.oauth2.provider.token.RemoteTokenServices;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

@Configuration
@EnableResourceServer
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {
    @Bean
    @LoadBalanced
    public RestTemplate restTemplate(RestTemplateBuilder builder){
        RestTemplate restTemplate = builder.build();
        /*为RestTemplate配置异常处理器0*/
        restTemplate.setErrorHandler(new DefaultResponseErrorHandler());
        return restTemplate;
    }


    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private JwtAccessTokenConverter accessTokenConverter;


    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
        resources.resourceId("website");
        resources.tokenStore(new JwtTokenStore(accessTokenConverter)).stateless(true);
        /* 配置令牌验证 */
        RemoteTokenServices remoteTokenServices = new RemoteTokenServices();
        remoteTokenServices.setAccessTokenConverter(accessTokenConverter);
        remoteTokenServices.setRestTemplate(restTemplate);
        remoteTokenServices.setCheckTokenEndpointUrl("http://localhost:8080/oauth/check_token");
        remoteTokenServices.setClientId("website");
        remoteTokenServices.setClientSecret("2020");

        resources.tokenServices(remoteTokenServices).stateless(true);
    }


    /* 配置资源拦截规则 */
    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED);
        // /save/no不受保护
        // /user/userInfo 受保护

        //可以访问login 和 user/userInfo ,   save/no不能访问
//        http
//                .authorizeRequests()
//                .anyRequest().authenticated()
//                .and()
//                .formLogin().permitAll()
//                .and()
//                .requestMatchers()
//                .antMatchers("/save/**")
//                .and().csrf().disable();


        //不能访问login，save下的资源没权限可以访问
//        http.formLogin().permitAll();
//        http.authorizeRequests()
//                .antMatchers("/save/**").permitAll()
//                .anyRequest().authenticated().and().csrf().disable();


        http.formLogin().permitAll();
        http.authorizeRequests()
                .antMatchers("/save/**").permitAll()
                .anyRequest().authenticated().and()
                .requestMatchers()
                .antMatchers("/save/**").and().csrf().disable();

        http.exceptionHandling().accessDeniedHandler(new OAuth2AccessDeniedHandler());
    }
}