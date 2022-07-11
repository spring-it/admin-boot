package cn.mesmile.admin.common.lock;

import java.util.concurrent.TimeUnit;

/**
 * @author zb
 * @Description
 */
public interface RedisLockClient {
    /**
     * 加锁
     * @param lockName
     * @param lockType
     * @param waitTime
     * @param leaseTime
     * @param timeUnit
     * @return
     * @throws InterruptedException
     */
    boolean tryLock(String lockName, LockTypeEnum lockType, long waitTime, long leaseTime, TimeUnit timeUnit) throws InterruptedException;

    /**
     * 解锁
     * @param lockName
     * @param lockType
     */
    void unLock(String lockName, LockTypeEnum lockType);

    /**
     * 加锁
     * @param lockName
     * @param lockType
     * @param waitTime
     * @param leaseTime
     * @param timeUnit
     * @param supplier
     * @param <T>
     * @return
     */
    <T> T lock(String lockName, LockTypeEnum lockType, long waitTime, long leaseTime, TimeUnit timeUnit, CheckedSupplier<T> supplier);

    /**
     * 公平锁方式加锁
     * @param lockName
     * @param waitTime
     * @param leaseTime
     * @param supplier
     * @param <T>
     * @return
     */
    default <T> T lockFair(String lockName, long waitTime, long leaseTime, CheckedSupplier<T> supplier) {
        return this.lock(lockName, LockTypeEnum.FAIR_LOCK, waitTime, leaseTime, TimeUnit.SECONDS, supplier);
    }

    /**
     *  可重入锁方式加锁
     * @param lockName
     * @param waitTime
     * @param leaseTime
     * @param supplier
     * @param <T>
     * @return
     */
    default <T> T lockReentrant(String lockName, long waitTime, long leaseTime, CheckedSupplier<T> supplier) {
        return this.lock(lockName, LockTypeEnum.REENTRANT_LOCK, waitTime, leaseTime, TimeUnit.SECONDS, supplier);
    }
}
