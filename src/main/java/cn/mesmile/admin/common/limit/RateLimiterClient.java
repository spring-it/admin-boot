package cn.mesmile.admin.common.limit;

import java.util.concurrent.TimeUnit;

/**
 * @author zb
 * @Description
 */
public interface RateLimiterClient {

    /**
     * 判断当前请求是否在允许范围内
     * @param keyName key名称
     * @param max 最大请求次数
     * @param ttl 超时时间
     * @param timeUnit 时间单位
     * @return 是否允许访问
     */
    boolean isAllow(String keyName, long max, long ttl, TimeUnit timeUnit);

}
