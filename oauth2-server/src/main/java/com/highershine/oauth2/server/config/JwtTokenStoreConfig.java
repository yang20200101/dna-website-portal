package com.highershine.oauth2.server.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

import java.io.IOException;

/**
 * jwt token配置
 */
@Configuration
public class JwtTokenStoreConfig {
    @Value("${oauth2.server.jwtSecret}")
    public String jwtSecret;

    @Bean
    public TokenStore jwtTokenStore()throws IOException {
        return new JwtTokenStore(jwtAccessTokenConverter());
    }

    @Bean
    public JwtAccessTokenConverter jwtAccessTokenConverter() throws IOException {
        JwtAccessTokenConverter accessTokenConverter = new
                JwtAccessTokenConverter();
        //配置JWT使用的秘钥
        accessTokenConverter.setSigningKey(jwtSecret);
        return accessTokenConverter;
    }

    @Bean
    public JwtTokenEnhancer jwtTokenEnhancer() {
        return new JwtTokenEnhancer();
    }
}
