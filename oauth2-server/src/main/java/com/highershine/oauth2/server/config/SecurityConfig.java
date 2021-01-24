package com.highershine.oauth2.server.config;

import com.highershine.oauth2.server.handlder.LoginExpireHandler;
import com.highershine.oauth2.server.handlder.LoginFailureHandler;
import com.highershine.oauth2.server.handlder.LoginSuccessHandler;
import com.highershine.oauth2.server.handlder.LogoutSuccessHandler;
import com.highershine.oauth2.server.service.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import javax.sql.DataSource;

/**
 * @Description: 安全框架配置类
 * @Author: xueboren
 * @Date: 2020/11/30 21:03
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private DataSource dataSource;
    @Autowired
    private UserDetailsServiceImpl userDetailsService;
    @Autowired
    @Qualifier("jwtTokenStore")
    private TokenStore tokenStore;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //关闭csrf
        http.cors().and().csrf().disable();
        // 授权配置
        /* 开启授权认证 */
        http.authorizeRequests()
                .antMatchers("/oauth/**").permitAll() //允许访问授权接口
                .anyRequest().authenticated();

        //登录页面
        http.formLogin().loginProcessingUrl("/login");
        // 登录成功或失败处理
        http.formLogin().successHandler(new LoginSuccessHandler())
                .failureHandler(new LoginFailureHandler());
        // 登出授权及处理
        http.logout().logoutUrl("/logout")
                .logoutSuccessHandler(new LogoutSuccessHandler(tokenStore)).permitAll();
        //登录超时 未登录
        http.exceptionHandling().authenticationEntryPoint(new LoginExpireHandler());
    }


    @Bean
    public PersistentTokenRepository persistentTokenRepository() {
        JdbcTokenRepositoryImpl jdbcTokenRepository = new JdbcTokenRepositoryImpl();
        jdbcTokenRepository.setDataSource(dataSource);

        // 自动建表
        //jdbcTokenRepository.setCreateTableOnStartup(true);

        return jdbcTokenRepository;
    }

    /** 授权服务配置需要用到这个bean */
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    /** 加密算法 */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
        auth.authenticationProvider(new MyAuthenticationProvider(userDetailsService));
    }
}
