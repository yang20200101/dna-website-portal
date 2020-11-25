package com.highershine.portal.config;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.Bucket;
import com.highershine.portal.common.enums.PolicyTypeEnum;
import com.highershine.portal.common.utils.PolicyUtils;
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
        //桶不存在 创建桶
        createBucketNotExists(s3Client, minIOPropertyConfig.getBucketName());
        return s3Client;
    }

    /**
     * 桶不存在 创建桶
     * @param s3
     */
    private void createBucketNotExists(AmazonS3 s3, String bucketName) {
        Bucket b = null;
        if (!s3.doesBucketExist(bucketName)) {
            //创建桶
            b = s3.createBucket(bucketName);
            //设置桶策略
            s3.setBucketPolicy(bucketName, PolicyUtils.getPolicyType(bucketName, PolicyTypeEnum.READ_WRITE));
        }
    }
}
