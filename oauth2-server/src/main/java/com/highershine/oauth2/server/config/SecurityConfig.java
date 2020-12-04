package com.highershine.oauth2.server.config;

import com.highershine.oauth2.server.service.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import javax.sql.DataSource;

/**
 * @Description: TODO
 * @Author: mizhanlei
 * @Date: 2020/11/30 21:03
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private DataSource dataSource;

    @Bean
    public PasswordEncoder getPw() {
        return new BCryptPasswordEncoder();
    }

    @Autowired
    private PersistentTokenRepository jdbcTokenRepository;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        /*
        http
                .formLogin().permitAll()
                // 登录界面
                //.loginPage("http://127.0.0.1:8080/login.html")
                //.loginProcessingUrl("/login")
                // 成功跳转地址
                //.successHandler(new HHAuthenticationSuccessHandler("http://127.0.0.1:8080/index.html"))
        .and()
                .authorizeRequests()
                // 不需要认证的资源
                .antMatchers("/login.html").permitAll()
                .antMatchers("/oauth/**", "/login/**").permitAll()
                .anyRequest().authenticated()
                // 自定义的权限认证
                //.anyRequest().access("@permissionServiceImpl.hasPermission(request, authentication)");
        .and().rememberMe().tokenRepository(jdbcTokenRepository)
                .rememberMeParameter("remember-me")
                // 默认两周
                .tokenValiditySeconds(60)
                // 自定义的登录逻辑
                .userDetailsService(userDetailsService)
        .and().logout()
                .logoutSuccessUrl("/login.html");

        http.csrf().disable();
        */


        http.authorizeRequests()
                .antMatchers("/oauth/**", "/login/**", "/logout/**", "/user/**")
                .permitAll()
                .anyRequest()
                .authenticated()
                .and()
                .formLogin()
                .permitAll()
                .and()
                .rememberMe()
                .tokenRepository(jdbcTokenRepository)
                .userDetailsService(userDetailsService)
                .and()
                .csrf()
                .disable();

    }


    @Bean
    public PersistentTokenRepository persistentTokenRepository() {
        JdbcTokenRepositoryImpl jdbcTokenRepository = new JdbcTokenRepositoryImpl();
        jdbcTokenRepository.setDataSource(dataSource);

        // 自动建表
        //jdbcTokenRepository.setCreateTableOnStartup(true);

        return jdbcTokenRepository;
    }

    @Bean(name = BeanIds.AUTHENTICATION_MANAGER)
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

//    @Override
//    protected void configure(AuthenticationManagerBuilder builder) throws Exception {
//        //注入用户信息，每次登录都会来这查询一次信息，因此不建议每次都向mysql查询，应该使用redis
//        //密码加密
//        builder.userDetailsService(userDetailsService).passwordEncoder(getPw());
//    }

}
