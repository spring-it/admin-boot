package cn.mesmile.admin.common.lock;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

/**
 * @author zb
 * @Description 分布式锁
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
@Documented
public @interface RedisLock {

    /**
     * 锁的名称
     */
    String value() default "";

    /**
     * 参数
     */
    String param() default "";

    /**
     * 等待锁超时时间，默认 30 秒
     * @return
     */
    long waitTime() default 30L;

    /**
     *   Redisson 默认的是 30秒
     *  自动解锁时间，自动解锁时间一定得大于方法执行时间，否则会导致锁提前释放，默认100
     * @return
     */
    long leaseTime() default 100L;

    /**
     * 时间单位，默认为 秒
     * @return
     */
    TimeUnit timeUnit() default TimeUnit.SECONDS;

    /**
     *  默认：可重入锁
     * @return
     */
    LockTypeEnum type() default LockTypeEnum.REENTRANT_LOCK;
}