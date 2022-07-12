package cn.mesmile.admin.common.limit;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.scripting.support.ResourceScriptSource;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * @author zb
 * @Description
 */
@Configuration(
        proxyBeanMethods = false
)
@EnableConfigurationProperties({RateLimiterProperties.class})
@ConditionalOnProperty(
        value = {"rate-limiter.redis.enabled"},
        havingValue = "true"
)
public class RateLimiterAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public RateLimiterClient rateLimiterClient(StringRedisTemplate redisTemplate) {
        RedisScript<List<Long>> listRedisScript = redisRateLimiterScript();
        return new RateLimiterClientImpl(redisTemplate, listRedisScript);
    }

    @Bean
    @ConditionalOnMissingBean
    public RateLimiterAspect rateLimiterAspect(RateLimiterClient rateLimiterClient) {
        return new RateLimiterAspect(rateLimiterClient);
    }

    private RedisScript<List<Long>> redisRateLimiterScript() {
        DefaultRedisScript redisScript = new DefaultRedisScript();
        redisScript.setScriptSource(new ResourceScriptSource(new ClassPathResource("/scripts/admin_rate_limiter.lua")));
        redisScript.setResultType(List.class);
        return redisScript;
    }

}
