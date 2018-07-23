package org.stathry.commons.dao.cache;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;

/**
 * TODO
 * @author dongdaiming
 */
public abstract class AbstractRedisManager<K,V> {
    
    @Autowired
    protected RedisTemplate<K, V> redisTemplate;

    @Autowired
    protected StringRedisTemplate stringRedisTemplate;
    
    public RedisSerializer<String> getStringSerializer() {
        return redisTemplate.getStringSerializer();
    }
    
    public RedisTemplate<K, V> getRedisTemplate() {
        return redisTemplate;
    }

    public StringRedisTemplate getStringRedisTemplate() {
        return stringRedisTemplate;
    }
}
