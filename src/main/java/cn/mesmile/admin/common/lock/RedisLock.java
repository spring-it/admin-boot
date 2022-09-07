package cn.mesmile.admin.common.lock;

import cn.mesmile.admin.common.constant.AdminConstant;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

/**
 * @author zb
 * @Description 分布式锁注解
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
@Documented
public @interface RedisLock {

    /**
     * 锁的key，唯一
     */
    String value();

    /**
     * redis锁的默认前缀
     */
    String prefix() default AdminConstant.REDIS_LOCK_PREFIX;

    /**
     * 参数，支持 spring el表达式 #读取方法参数和@读取spring bean
     * 例如 @RedisLock(value = "buy",param = "#request.getLocalPort()")
     * 当有多个表达式的时候中间用 ; 分割
     */
    String param() default "";

    /**
     * 等待获取锁超时时间，默认 30 秒
     */
    long waitTime() default 30L;

    /**
     *   Redisson 默认的是 30秒
     *  自动解锁时间，【自动解锁时间一定得大于方法执行时间，否则会导致锁提前释放】，默认100 秒,
     *
     *      注意：
     *      ① 这里采用 lock.tryLock(waitTime, leaseTime, timeUnit) 传入时间后，底层锁[不会自动续期]，即：没有看门狗锁续期功能
     *
     *      ② 如果底层采用 lock.lock() 方法，则默认过期时间 30 秒，每隔10会检查是否需要续期 【有看门狗/锁续期 功能】
     */
    long leaseTime() default 100L;

    /**
     * 时间单位，默认为 秒
     */
    TimeUnit timeUnit() default TimeUnit.SECONDS;

    /**
     *  默认：可重入锁
     */
    LockTypeEnum type() default LockTypeEnum.REENTRANT_LOCK;

}