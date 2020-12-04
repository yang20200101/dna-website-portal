package com.highershine.oauth2.server.config;

import com.highershine.oauth2.server.entity.SysUser;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;

import java.util.HashMap;
import java.util.Map;

/**
 * @Description: TODO
 * @Author: mizhanlei
 * @Date: 2020/12/1 22:32
 */
public class JwtTokenEnhancer implements TokenEnhancer {

    @Override
    public OAuth2AccessToken enhance(OAuth2AccessToken oAuth2AccessToken, OAuth2Authentication oAuth2Authentication) {
        SysUser sysUser = (SysUser) oAuth2Authentication.getPrincipal();
        Map<String, Object> map = new HashMap<>();
        map.put("name", sysUser.getUsername());
        map.put("authorities", sysUser.getAuthorities());
        map.put("idCardNo", "sysUser.idCardNo");
        map.put("orgCode", "sysUser.orgCode");
        ((DefaultOAuth2AccessToken)oAuth2AccessToken).setAdditionalInformation(map);
        return oAuth2AccessToken;
    }

}
