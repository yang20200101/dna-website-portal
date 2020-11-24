package com.highershine.portal;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.highershine.portal.*")
@MapperScan(basePackages = "com.highershine.portal.common.mapper")
@EnableConfigurationProperties
public class WebsitePortalApplication {

    public static void main(String[] args) {
        SpringApplication.run(WebsitePortalApplication.class, args);
    }

}
