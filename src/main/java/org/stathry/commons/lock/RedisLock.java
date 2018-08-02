package org.stathry.commons.lock;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 分布式锁
 * Created by dongdaiming on 2018-07-28 12:50
 */
// https://blog.csdn.net/lpayit/article/details/71525905
public class RedisLock implements DistributedLock{

    private static final Logger LOGGER = LoggerFactory.getLogger(RedisLock.class);

    private RedisTemplate<String, Long> redisTemplate;

    private final long LOCK_EXPIRE_MS_MAX = TimeUnit.SECONDS.toMillis(180);

    private long lockExpireMS = 10 * 1000;
    private long lockTimeoutMS = 30 * 1000;

    private final int SLEEP_MS = 10;
    private final String key;
    private final Lock lock = new ReentrantLock();
    private final AtomicLong counter = new AtomicLong();

    public RedisLock(RedisTemplate redisTemplate, String key) {
        checkKeyAndTime(redisTemplate, lockExpireMS, lockTimeoutMS, key);
        this.redisTemplate = redisTemplate;
        this.key = key;
    }

    public RedisLock(RedisTemplate redisTemplate, long lockExpireMS, long lockTimeoutMS, String key) {
        checkKeyAndTime(redisTemplate, lockExpireMS, lockTimeoutMS, key);
        this.lockExpireMS = lockExpireMS;
        this.lockTimeoutMS = lockTimeoutMS;
        this.redisTemplate = redisTemplate;
        this.key = key;
    }

    @Override
    public boolean lock() throws InterruptedException {
        long begin = System.currentTimeMillis();
        long curMS;
        Long oldValue;
        ValueOperations<String, Long> ops = redisTemplate.opsForValue();
        if (lock.tryLock(lockTimeoutMS, TimeUnit.MILLISECONDS)) {
            for (; ((curMS = System.currentTimeMillis()) - begin) < lockTimeoutMS; ) {
                if (ops.setIfAbsent(key, curMS)) {
                    ops.set(key, curMS, lockExpireMS, TimeUnit.MILLISECONDS);
                    return true;
                }
                oldValue = ops.get(key);
                if (oldValue == null) {
                    ops.set(key, curMS, lockExpireMS, TimeUnit.MILLISECONDS);
                    return true;
                } else if (curMS > oldValue) {
                    if (oldValue == ops.getAndSet(key, curMS)) {
                        ops.set(key, curMS, lockExpireMS, TimeUnit.MILLISECONDS);
                        return true;
                    }
                }
                try {
                    Thread.sleep(SLEEP_MS);
                } catch (InterruptedException e) {
                    // ignore
                }
            }
        }

        return false;
    }

    @Override
    public boolean tryLock() {
        if(redisTemplate.opsForValue().setIfAbsent(key, System.currentTimeMillis())) {
            redisTemplate.expire(key, lockExpireMS, TimeUnit.MILLISECONDS);
            return true;
        }
        return false;
    }

    @Override
    public void unlock() {
        redisTemplate.delete(key);
        lock.unlock();
        long c = counter.incrementAndGet();
        if(c % 1000 == 0) {
        LOGGER.info("thread {} released lock, counter {}.", Thread.currentThread().getName(), c);
        }
    }

    private void checkKeyAndTime(RedisTemplate redisTemplate, long lockExpireMS, long lockTimeoutMS, String key) {
        if(redisTemplate == null || StringUtils.isBlank(key) || lockExpireMS <= 0 || lockTimeoutMS <= 0
                || lockExpireMS > LOCK_EXPIRE_MS_MAX || lockTimeoutMS > LOCK_EXPIRE_MS_MAX) {
            throw new IllegalArgumentException("illegal arguments: redisTemplate=" + redisTemplate
                    +  ", key =" + key + ", lockExpireMS=" + lockExpireMS + ", lockTimeoutMS=" + lockTimeoutMS);
        }
    }
}
