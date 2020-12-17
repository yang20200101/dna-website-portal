package com.highershine.oauth2.server.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.*;
import org.springframework.stereotype.Service;

@Service("redisUtil")
@Configuration
public class RedisUtil {

    private static final Logger LOGGER = LogManager.getLogger(RedisUtil.class);

    @Autowired
    private StringRedisTemplate redisTemplate;

    /**
     * 实例化 HashOperations 对象,可以使用 Hash 类型操作
     */
    @Bean
    public HashOperations<String, String, String> hashOperations() {
        return redisTemplate.opsForHash();
    }

    /**
     * 实例化 ValueOperations 对象,可以使用 String 操作
     */
    @Bean(value = "valueOperations")
    public ValueOperations<String, String> valueOperations() {
        return redisTemplate.opsForValue();
    }

    /**
     * 实例化 ListOperations 对象,可以使用 List 操作
     * @return
     */
    @Bean
    public ListOperations<String, String> listOperations() {
        return redisTemplate.opsForList();
    }

    /**
     * 实例化 SetOperations 对象,可以使用 Set 操作
     */
    @Bean
    public SetOperations<String, String> setOperations() {
        return redisTemplate.opsForSet();
    }

    /**
     * 实例化 ZSetOperations 对象,可以使用 ZSet 操作
     */
    @Bean
    public ZSetOperations<String, String> zSetOperations() {
        return redisTemplate.opsForZSet();
    }

    /**
     * 移除缓存
     * @param key
     * @return
     */
    public boolean remove(String key) {
        try {
            redisTemplate.delete(key);
            return true;
        } catch (Throwable t) {
            LOGGER.error("获取缓存失败key[" + key + ", error[" + t + "]");
        }
        return false;
    }

}
