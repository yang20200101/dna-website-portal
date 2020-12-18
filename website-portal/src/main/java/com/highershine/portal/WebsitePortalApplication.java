package com.highershine.portal;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;

@SpringBootApplication
@ComponentScan(basePackages = "com.highershine.portal.*")
@MapperScan(basePackages = "com.highershine.portal.common.mapper")
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
public class WebsitePortalApplication {

    public static void main(String[] args) {
        SpringApplication.run(WebsitePortalApplication.class, args);
    }

}
