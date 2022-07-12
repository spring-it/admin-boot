package cn.mesmile.admin.common.lock;

import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;

import java.util.concurrent.TimeUnit;

/**
 * @author zb
 * @Description 分布式锁redisson核心 tryLock   unlock
 */
public class RedisLockClientImpl implements RedisLockClient {

    private final RedissonClient redissonClient;

    public RedisLockClientImpl(final RedissonClient redissonClient) {
        this.redissonClient = redissonClient;
    }

    @Override
    public boolean tryLock(String lockName, LockTypeEnum lockType, long waitTime, long leaseTime, TimeUnit timeUnit) throws InterruptedException {
        RLock lock = this.getLock(lockName, lockType);
        return lock.tryLock(waitTime, leaseTime, timeUnit);
    }

    @Override
    public void unLock(String lockName, LockTypeEnum lockType) {
        RLock lock = this.getLock(lockName, lockType);
        // 判断是否持有锁，若持有锁才进行解锁
        if (lock.isLocked() && lock.isHeldByCurrentThread()) {
            lock.unlock();
        }
    }

    /**
     *  通过锁名称和锁类型，获取锁
     * @param lockName 锁名称
     * @param lockType 锁类型
     * @return 锁
     */
    private RLock getLock(String lockName, LockTypeEnum lockType) {
        RLock lock;
        if (LockTypeEnum.REENTRANT_LOCK == lockType) {
            lock = this.redissonClient.getLock(lockName);
        } else {
            lock = this.redissonClient.getFairLock(lockName);
        }
        return lock;
    }

    @Override
    public <T> T lock(String lockName, LockTypeEnum lockType, long waitTime, long leaseTime, TimeUnit timeUnit, ThrowSupplier<T> supplier) {
        try {
            boolean result = this.tryLock(lockName, lockType, waitTime, leaseTime, timeUnit);
            if (!result) {
                return null;
            }
            // 执行方法
            return supplier.get();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        } finally {
            this.unLock(lockName, lockType);
        }
    }

}

