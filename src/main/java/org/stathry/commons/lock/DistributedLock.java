package org.stathry.commons.lock;

/**
 * 分布式锁
 * Created by dongdaiming on 2018-07-28 12:50
 */
public interface DistributedLock {

    boolean lock() throws InterruptedException;

    boolean lock(long lockTimeoutMS) throws InterruptedException;

    void unlock();

}
