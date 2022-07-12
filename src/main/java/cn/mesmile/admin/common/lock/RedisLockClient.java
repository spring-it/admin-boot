package cn.mesmile.admin.common.lock;

import java.util.concurrent.TimeUnit;

/**
 * @author zb
 * @Description
 */
public interface RedisLockClient {
    /**
     * 加锁
     *
     * @param lockName  锁的名称
     * @param lockType  锁的类型 可重入锁或者公平锁
     * @param waitTime  等待锁超时时间，默认 30 秒
     * @param leaseTime 自动解锁时间
     * @param timeUnit  时间单位
     * @return 是否加锁成功
     * @throws InterruptedException
     */
    boolean tryLock(String lockName, LockTypeEnum lockType, long waitTime, long leaseTime, TimeUnit timeUnit) throws InterruptedException;

    /**
     * 解锁
     *
     * @param lockName
     * @param lockType
     */
    void unLock(String lockName, LockTypeEnum lockType);

    /**
     * 加锁
     *
     * @param lockName  锁的名称
     * @param lockType  锁的类型 可重入锁或者公平锁
     * @param waitTime  等待锁超时时间，默认 30 秒
     * @param leaseTime 自动解锁时间
     * @param timeUnit  时间单位
     * @param supplier  操作
     * @return 执行结果
     */
    <T> T lock(String lockName, LockTypeEnum lockType, long waitTime, long leaseTime, TimeUnit timeUnit, ThrowSupplier<T> supplier);
}
