package cn.mesmile.admin.common.limit;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author zb
 * @Description
 */
@Data
@ConfigurationProperties("rate-limiter.redis")
public class RateLimiterProperties {

    /**
     *  是否开启限流注解
     */
    private Boolean enabled;

    public RateLimiterProperties(){
        // 默认不开启
        this.enabled = Boolean.FALSE;
    }

}
