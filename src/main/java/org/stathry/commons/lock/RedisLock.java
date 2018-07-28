package org.stathry.commons.lock;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * 分布式锁
 * Created by dongdaiming on 2018-07-28 12:50
 */
public class RedisLock implements DistributedLock {

    private StringRedisTemplate stringRedisTemplate;

    private final long LOCK_EXPIRE_MS_MAX = TimeUnit.SECONDS.toMillis(60);
    private final long LOCK_TIMEOUT_MS_MAX = TimeUnit.SECONDS.toMillis(10);

    private final long LOCK_EXPIRE_MS;
    private final long LOCK_TIMEOUT_MS;

    private final int RANDOM_SLEEP_MS = 100;
    private final String DEFAULT_VALUE = "1";
    private final String KEY;
    private final Random RANDOM = new Random();

    public RedisLock(StringRedisTemplate stringRedisTemplate, String key) {
        checkKeyAndTime(stringRedisTemplate, key, LOCK_EXPIRE_MS_MAX, LOCK_TIMEOUT_MS_MAX);
        KEY = key;
        LOCK_EXPIRE_MS = LOCK_EXPIRE_MS_MAX;
        LOCK_TIMEOUT_MS = LOCK_TIMEOUT_MS_MAX;
        this.stringRedisTemplate = stringRedisTemplate;
    }

    public RedisLock(StringRedisTemplate stringRedisTemplate, String key, long lockExpireMS, long lockTimeoutMS) {
        checkKeyAndTime(stringRedisTemplate, key, lockExpireMS, lockTimeoutMS);
        KEY = key;
        LOCK_EXPIRE_MS = lockExpireMS;
        LOCK_TIMEOUT_MS = lockTimeoutMS;
        this.stringRedisTemplate = stringRedisTemplate;
    }

    private void checkKeyAndTime(StringRedisTemplate stringRedisTemplate, String key, long lockExpireMS, long lockTimeoutMS) {
        if(stringRedisTemplate == null || key == null || lockExpireMS <= 0 || lockTimeoutMS <= 0
                || lockExpireMS > LOCK_EXPIRE_MS_MAX || lockTimeoutMS > LOCK_TIMEOUT_MS_MAX) {
            throw new IllegalArgumentException("illegal arguments: stringRedisTemplate=" + stringRedisTemplate + ", key=" + key
                    + ", lockExpireMS=" + lockExpireMS + ", lockTimeoutMS=" + lockTimeoutMS);
        }
    }

    @Override
    public synchronized boolean lock() {
        Random r = RANDOM;
        long begin = System.currentTimeMillis();
        ValueOperations<String, String> ops = stringRedisTemplate.opsForValue();
        for (; (System.currentTimeMillis() - begin) < LOCK_TIMEOUT_MS; ) {
            if(ops.setIfAbsent(KEY, DEFAULT_VALUE)) {
                stringRedisTemplate.expire(KEY, LOCK_EXPIRE_MS, TimeUnit.MILLISECONDS);
                return true;
            }
            try {
                Thread.sleep(r.nextInt(RANDOM_SLEEP_MS));
            } catch (InterruptedException e) {
                // ignore
            }
        }
        return false;
    }

    @Override
    public synchronized boolean tryLock() {
        if(stringRedisTemplate.opsForValue().setIfAbsent(KEY, DEFAULT_VALUE)) {
            stringRedisTemplate.expire(KEY, LOCK_EXPIRE_MS, TimeUnit.MILLISECONDS);
            return true;
        }
        return false;
    }

    @Override
    public synchronized void unlock() {
        stringRedisTemplate.delete(KEY);
    }
}
