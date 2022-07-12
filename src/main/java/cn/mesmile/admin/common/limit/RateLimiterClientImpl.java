package cn.mesmile.admin.common.limit;

import cn.hutool.core.collection.CollUtil;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;

import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author zb
 * @Description
 */
public class RateLimiterClientImpl implements RateLimiterClient {

    private final StringRedisTemplate redisTemplate;
    private final RedisScript<List<Long>> script;

    public RateLimiterClientImpl(StringRedisTemplate redisTemplate, RedisScript<List<Long>> script) {
        this.redisTemplate = redisTemplate;
        this.script = script;
    }

    @Override
    public boolean isAllow(String keyName, long max, long ttl, TimeUnit timeUnit) {
        List<String> keys = Collections.singletonList(keyName);
        // 当前时间毫秒数
        long now = Instant.now().toEpochMilli();
        // 超时时间
        long ttlMillis = timeUnit.toMillis(ttl);
        // 过期时间点
        long expired = now - ttlMillis;
        List<Long> results = (List)redisTemplate
                // 注意这里必须转为 String,否则会报错 java.lang.Long cannot be cast to java.lang.String
                .execute(script, keys, new Object[]{now + "", ttlMillis + "", expired + "", max + ""});
        if (results != null && results.size() > 0) {
            Long result = (Long)results.get(0);
            return result != 0L;
        } else {
            return false;
        }
    }
}
