package com.highershine.portal.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @Description
 * @Author zxk
 * @Date 2020/4/17 11:26
 **/
@Data
@Component
@ConfigurationProperties(prefix = "minio")
public class MinIOPropertyConfig {

    private String endPoint;

    private String accessKey;

    private String secretKey;

    private Integer connTimeOut;

    private Integer maxConnections;

    private Integer maxErrorRetry;

    private Integer socketTimeOut;

    private String bucketName;
}
