package com.highershine.portal.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @Description 接口配置类
 * @Author zxk
 * @Date 2020/4/8 14:40
 **/
@Data
@Component
@ConfigurationProperties(prefix = "person.query")
public class InterfaceConfig {
    private String addr;
    private String salt;
    private String systemKey;
}
