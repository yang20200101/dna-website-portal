package com.highershine.oauth2.server.config;

import com.highershine.oauth2.server.service.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

/**
 * 授权服务配置
 */
@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

    @Autowired
    @Qualifier("jwtTokenStore")
    private TokenStore tokenStore;

    @Autowired
    private DataSource dataSource;

    @Autowired
    private JwtAccessTokenConverter jwtAccessTokenConverter;

    @Autowired
    private JwtTokenEnhancer jwtTokenEnhancer;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * 配置一个客户端
     */
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.jdbc(dataSource).passwordEncoder(passwordEncoder);
    }

    /** 配置token管理 */
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        TokenEnhancerChain chain = new TokenEnhancerChain();
        List<TokenEnhancer> list = new ArrayList<>();
        list.add(jwtTokenEnhancer);
        list.add(jwtAccessTokenConverter);
        chain.setTokenEnhancers(list);

        //通过注入密码授权被打开AuthenticationManager
        endpoints.authenticationManager(authenticationManager)
                .tokenStore(tokenStore)
                //刷新令牌授权将包含对用户详细信息的检查，以确保该帐户仍然活动,因此需要配置userDetailsService
                .userDetailsService(userDetailsService)
                //配置令牌生成
                .accessTokenConverter(jwtAccessTokenConverter)
                .tokenEnhancer(chain);

//                //该字段设置设置refresh token是否重复使用,true:reuse;false:no reuse.
//                .reuseRefreshTokens(true)

    }

    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        security.tokenKeyAccess("permitAll()") //允许所有人请求令牌
                .checkTokenAccess("isAuthenticated()") //已验证的客户端才能请求check_token端点
                .allowFormAuthenticationForClients();

        // 单点登录配置
//        security.tokenKeyAccess("isAuthenticated()");
    }
}
