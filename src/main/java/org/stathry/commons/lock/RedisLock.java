package org.stathry.commons.lock;

import org.stathry.commons.dao.RedisManager;
import org.stathry.commons.utils.ApplicationContextUtils;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * redis分布式锁
 * Created by dongdaiming on 2018-07-28 12:50
 */
// https://blog.csdn.net/lpayit/article/details/71525905
public class RedisLock implements DistributedLock {

    private static final RedisManager redisManager = ApplicationContextUtils.getBean(RedisManager.class);

    private static final long DEFAULT_LOCK_EXPIRE = 30 * 1000;
    private static final long DEFAULT_LOCK_TIMEOUT = 10 * 1000;
    private static final long DEFAULT_SLEEP = 50;
    private static final boolean DEFAULT_FAIR_LOCK = false;

    private final String key;
    private final long lockExpire;
    private final long lockTimeout;
    private final long sleepTime;
    private final Lock lock;

    public RedisLock(String key) {
        this.key = key;
        this.lockExpire = DEFAULT_LOCK_EXPIRE;
        this.lockTimeout = DEFAULT_LOCK_TIMEOUT;
        this.lock = new ReentrantLock(DEFAULT_FAIR_LOCK);
        this.sleepTime = DEFAULT_SLEEP;
    }

    public RedisLock(String key, long lockExpire, long lockTimeout, long sleep, boolean fair) {
        this.key = key;
        this.lockExpire = lockExpire;
        this.lockTimeout = lockTimeout;
        this.sleepTime = sleep;
        this.lock = new ReentrantLock(fair);
    }

    private boolean setNX() {
        return redisManager.setNX(key, 1, lockExpire);
    }

    @Override
    public boolean lock() throws InterruptedException {
        return lock(lockTimeout);
    }

    @Override
    public boolean lock(long lockTimeoutMS) throws InterruptedException {
        long beginTime = System.currentTimeMillis();
        boolean isLocked = lock.tryLock(lockTimeoutMS, TimeUnit.MILLISECONDS);
        if (isLocked) {
            while (System.currentTimeMillis() - beginTime < lockTimeoutMS) {
                if (setNX()) {
                    return true;
                }
                Thread.sleep(sleepTime);
            }
            lock.unlock();
        }
        return false;
    }

    @Override
    public void unlock() {
        redisManager.delete(key);
        lock.unlock();
    }

}
