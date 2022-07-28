package cn.mesmile.admin.modules.social.cache;

import me.zhyd.oauth.cache.AuthCacheConfig;
import me.zhyd.oauth.cache.AuthStateCache;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.util.concurrent.TimeUnit;

/**
 * @author zb
 * @Description 第三方认证缓存
 */
public class SocialAuthStateRedisCache implements AuthStateCache {

    private final RedisTemplate<String, Object> redisTemplate;

    private final ValueOperations<String, Object> valueOperations;

    @Override
    public void cache(String key, String value) {
        valueOperations.set(key, value, AuthCacheConfig.timeout, TimeUnit.MILLISECONDS);
    }

    @Override
    public void cache(String key, String value, long timeout) {
        valueOperations.set(key, value, timeout, TimeUnit.MILLISECONDS);
    }

    @Override
    public String get(String key) {
        return String.valueOf(valueOperations.get(key));
    }

    @Override
    public boolean containsKey(String key) {
        return redisTemplate.hasKey(key);
    }

    public SocialAuthStateRedisCache(final RedisTemplate<String, Object> redisTemplate, final ValueOperations<String, Object> valueOperations) {
        this.redisTemplate = redisTemplate;
        this.valueOperations = valueOperations;
    }
}
