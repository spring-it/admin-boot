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
     * 过期时间,单位 秒
     */
    private Integer expireTime = 7200;
}
