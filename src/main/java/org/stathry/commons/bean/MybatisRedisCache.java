package org.stathry.commons.bean;

import org.apache.ibatis.cache.Cache;
import org.mybatis.caches.redis.RedisCache;
import org.stathry.commons.utils.ApplicationContextUtils;

import java.util.concurrent.locks.ReadWriteLock;

/**
 * MybatisRedisCache
 * Created by dongdaiming on 2018-11-30 18:47
 */
public class MybatisRedisCache implements Cache {


    private RedisCache redisCache;
    private static final RedisManager redisManager = ApplicationContextUtils.getBean(RedisManager.class);
    private static final int TIMEOUT = 300000;

    public MybatisRedisCache(final String id) {
        redisCache = new RedisCache(id);
    }

    @Override
    public String getId() {
        return redisCache.getId();
    }

    @Override
    public void putObject(Object key, Object value) {
        redisCache.putObject(key, value);
        // expire
    }

    @Override
    public Object getObject(Object key) {
        return redisCache.getObject(key);
    }

    @Override
    public Object removeObject(Object key) {
        return redisCache.removeObject(key);
    }

    @Override
    public void clear() {
        redisCache.clear();
    }

    @Override
    public int getSize() {
        return redisCache.getSize();
    }

    @Override
    public ReadWriteLock getReadWriteLock() {
        return redisCache.getReadWriteLock();
    }
}
