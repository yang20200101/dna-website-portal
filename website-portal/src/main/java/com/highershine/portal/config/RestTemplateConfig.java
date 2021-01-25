package com.highershine.portal.config;

import com.highershine.portal.handlder.CustomerResponseErrorHandler;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * @Description: RestTemplate Bean
 * @Author: xueboren
 * @Date: 2020/1/19 16:35
 */
@Configuration
public class RestTemplateConfig {
    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder){
        RestTemplate restTemplate = builder.build();
        /*为RestTemplate配置异常处理器0*/
        restTemplate.setErrorHandler(new CustomerResponseErrorHandler());
        return restTemplate;
    }
}
