package com.highershine.oauth2.server.config;

import com.highershine.oauth2.server.utils.JwtUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

/**
 * @Description: TODO
 * @Author: mizhanlei
 * @Date: 2020/12/1 22:13
 */
@Configuration
public class JwtTokenStoreConfig {

    @Bean
    public TokenStore jwtTokenStore() {
        return new JwtTokenStore(jwtAccessTokenConverter());
    }

    @Bean
    public JwtAccessTokenConverter jwtAccessTokenConverter() {
        JwtAccessTokenConverter accessTokenConverter = new
                JwtAccessTokenConverter();
        //配置JWT使用的秘钥
        accessTokenConverter.setSigningKey(JwtUtils.SECRET);
        return accessTokenConverter;
    }

    @Bean
    public JwtTokenEnhancer jwtTokenEnhancer() {
        return new JwtTokenEnhancer();
    }
}
