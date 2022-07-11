package cn.mesmile.admin.common.lock;

import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;

import java.util.concurrent.TimeUnit;

/**
 * @author zb
 * @Description
 */
public class RedisLockClientImpl implements RedisLockClient {

    private final RedissonClient redissonClient;

    @Override
    public boolean tryLock(String lockName, LockTypeEnum lockType, long waitTime, long leaseTime, TimeUnit timeUnit) throws InterruptedException {
        RLock lock = this.getLock(lockName, lockType);
        return lock.tryLock(waitTime, leaseTime, timeUnit);
    }

    @Override
    public void unLock(String lockName, LockTypeEnum lockType) {
        RLock lock = this.getLock(lockName, lockType);
        if (lock.isLocked() && lock.isHeldByCurrentThread()) {
            lock.unlock();
        }
    }

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
    public <T> T lock(String lockName, LockTypeEnum lockType, long waitTime, long leaseTime, TimeUnit timeUnit, CheckedSupplier<T> supplier) {
        Object object = null;
        try {
            boolean result = this.tryLock(lockName, lockType, waitTime, leaseTime, timeUnit);
            if (!result) {
                return null;
            }
            object = supplier.get();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        } finally {
            this.unLock(lockName, lockType);
        }
        return (T) object;
    }

    public RedisLockClientImpl(final RedissonClient redissonClient) {
        this.redissonClient = redissonClient;
    }
}

