package com.highershine.portal.controller;

import com.amazonaws.services.s3.AmazonS3;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @Description
 * @Author zxk
 * @Date 2020/4/17 13:30
 **/
@RunWith(SpringRunner.class)
@SpringBootTest
public class MinIOTest {
    @Autowired
    private AmazonS3 amazonS3;
    @Test
    public void testClient(){
        Assert.assertNotNull(amazonS3);
        boolean exist = amazonS3.doesBucketExist("testbucket");
        Assert.assertTrue(exist);
    }
}
