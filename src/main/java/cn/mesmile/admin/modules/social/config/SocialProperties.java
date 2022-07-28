package cn.mesmile.admin.modules.social.config;

import com.google.common.collect.Maps;
import lombok.Getter;
import lombok.Setter;
import me.zhyd.oauth.config.AuthConfig;
import me.zhyd.oauth.config.AuthDefaultSource;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @author zb
 * @Description
 */
@Component
@Getter
@Setter
@ConfigurationProperties(prefix = "social")
public class SocialProperties {
    /**
     * 是否开启第三方登录
     */
    private Boolean enabled = Boolean.FALSE;
    /**
     * 当前应用回调地址domain
     */
    private String domain;
    /**
     *  数据来源
     */
    private Map<AuthDefaultSource, AuthConfig> oauth = new HashMap<>();

    /**
     * 别名
     */
    private Map<String, String> alias = new HashMap<>();

}
