package org.stathry.commons.lock;

/**
 * 分布式锁
 * Created by dongdaiming on 2018-07-28 12:50
 */
public interface DistributedLock {

    /**
     * 获取锁(阻塞式)
     * @return 是否锁定成功
     */
    boolean lock();

    /**
     * 获取锁(非阻塞式)
     * @return
     */
    boolean tryLock();

    /**
     * 解锁
     */
    void unlock();

}
