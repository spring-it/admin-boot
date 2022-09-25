package cn.mesmile.admin.modules.auth.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

/**
 * @author zb
 * @Description
 */
@Data
@ConfigurationProperties(prefix = "jwt")
public class JwtProperties {

    /**
     * token 请求头
     */
    private String tokenHeader = "Authorization";

    /**
     * token前缀
     */
    private String tokenPrefix = "Bearer";

    /**
     * 秘钥
     */
    private String secret = "zb-admin-security";

    /**
     * token过期时间,单位 秒，默认 2个小时
     */
    private Integer expireTime = 7200;

    /**
     * 刷新token过期时间,单位 秒，默认 7 天
     */
    private Integer refreshExpireTime = 3600 * 24 * 7;
}
