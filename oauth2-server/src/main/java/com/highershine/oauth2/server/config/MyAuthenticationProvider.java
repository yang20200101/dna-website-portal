package com.highershine.oauth2.server.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class MyAuthenticationProvider extends DaoAuthenticationProvider {

    public MyAuthenticationProvider(UserDetailsService userDetailsService) {
        super();
        // 这个地方一定要对userDetailsService赋值，不然userDetailsService是null (这个坑有点深)
        setUserDetailsService(userDetailsService);
        setPasswordEncoder(new BCryptPasswordEncoder());
    }


    protected void additionalAuthenticationChecks(UserDetails userDetails,
                                                  UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
        /*
        直接通过，校验逻辑写在userDetailsService中
        不写在该方法中，是由于：
        该方法中抛出的异常，都会转为BadCredentialsException（hideUserNotFoundExceptions改为false无效）
        */
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(aClass);
    }
}
