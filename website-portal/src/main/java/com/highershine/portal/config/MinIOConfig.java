package com.highershine.portal.config;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Description
 * @Author zxk
 * @Date 2020/4/17 13:16
 **/
@Configuration
public class MinIOConfig {
    @Autowired
    private MinIOPropertyConfig minIOPropertyConfig;

    @Bean("minIOClient")
    public AmazonS3 getMinIOClient() {
        AWSCredentials credentials = new BasicAWSCredentials(minIOPropertyConfig.getAccessKey(), minIOPropertyConfig.getSecretKey());
        ClientConfiguration clientConfiguration = new ClientConfiguration();
        clientConfiguration.setSignerOverride("AWSS3V4SignerType");
        clientConfiguration.setConnectionTimeout(minIOPropertyConfig.getConnTimeOut());
        clientConfiguration.setMaxConnections(minIOPropertyConfig.getMaxConnections());
        clientConfiguration.setMaxErrorRetry(minIOPropertyConfig.getMaxErrorRetry());
        clientConfiguration.setSocketTimeout(minIOPropertyConfig.getSocketTimeOut());

        AmazonS3 s3Client = AmazonS3ClientBuilder
                .standard()
                .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(minIOPropertyConfig.getEndPoint(),
                        Regions.AP_NORTHEAST_1.name()))
                .withPathStyleAccessEnabled(true)
                .withClientConfiguration(clientConfiguration)
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .build();

        return s3Client;
    }
}
