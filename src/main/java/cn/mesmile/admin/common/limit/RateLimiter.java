package cn.mesmile.admin.common.limit;

import cn.mesmile.admin.common.constant.AdminConstant;

import java.lang.annotation.*;
import java.util.concurrent.TimeUnit;

/**
 * @author zb
 * @Description 请求频率限制器
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
@Documented
public @interface RateLimiter {

    /**
     * 自定义缓存key,可选
     */
    String customKey() default "";

    /**
     * 限流key，前缀
     */
    String prefix() default AdminConstant.RATE_LIMITER_PREFIX;

    /**
     * 方法参数，支持spring el 表达式例如  #reques.getValue()
     * 当有多个表达式的时候中间用 ; 分割
     */
    String param() default "";

    /**
     * 默认提示消息
     */
    String msg() default "您的访问次数已超限，请稍后重试";

    /**
     * 单位时间内的最大访问量，默认100次
     */
    long max() default 100L;

    /**
     * 超时时间，默认 1 分钟
     */
    long ttl() default 1L;

    /**
     * 时间单位,默认单位 分钟
     */
    TimeUnit timeUnit() default TimeUnit.MINUTES;

    /**
     * 限制模式,默认：同一个接口针对所有ip进行限制
     */
    LimiterModeEnum limiterMode() default LimiterModeEnum.LIMITER_ALL;

}
