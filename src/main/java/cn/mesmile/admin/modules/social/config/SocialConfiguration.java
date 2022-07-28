package cn.mesmile.admin.modules.social.config;

import cn.mesmile.admin.modules.social.cache.SocialAuthStateRedisCache;
import com.xkcoding.http.HttpUtil;
import com.xkcoding.http.support.Http;
import com.xkcoding.http.support.httpclient.HttpClientImpl;
import me.zhyd.oauth.cache.AuthStateCache;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * @author zb
 * @Description 第三方登录
 */
@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties({SocialProperties.class})
@ConditionalOnProperty(
        value = {"social.enabled"},
        havingValue = "true"
)
public class SocialConfiguration {

    @Bean
    @ConditionalOnMissingBean({Http.class})
    public Http simpleHttp() {
        HttpClientImpl httpClient = new HttpClientImpl();
        HttpUtil.setHttp(httpClient);
        return httpClient;
    }

    @Bean
    @ConditionalOnMissingBean({AuthStateCache.class})
    public AuthStateCache authStateCache(RedisTemplate<String, Object> redisTemplate) {
        return new SocialAuthStateRedisCache(redisTemplate, redisTemplate.opsForValue());
    }

}

