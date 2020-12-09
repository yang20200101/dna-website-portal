package com.highershine.oauth2.server.config;

import com.highershine.oauth2.server.handlder.HHAuthenticationSuccessHandler;
import com.highershine.oauth2.server.handlder.LoginExpireHandler;
import com.highershine.oauth2.server.service.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
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


    @Autowired
    private PersistentTokenRepository jdbcTokenRepository;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //关闭csrf
        http.cors().and().csrf().disable();
        // 授权配置
        http.authorizeRequests().anyRequest().authenticated();
        //登录页面
        http.formLogin().loginProcessingUrl("/login");
        // 登录成功处理
        http.formLogin().successHandler(new HHAuthenticationSuccessHandler());
        // 登出授权
        http.logout().permitAll();
        //登录超时 未登录
        http.exceptionHandling().authenticationEntryPoint(new LoginExpireHandler());
        // rest无状态 无session
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        // 配置token验证过滤器
        http.addFilterBefore(new JWTAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);

       /* http.authorizeRequests()
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
                .disable();*/
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
    }

}
