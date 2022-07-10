package cn.mesmile.admin.common.filter.space;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.HashSet;
import java.util.Set;

/**
 * 去除请求中字符串两边空格
 *
 * @author zb
 */
@ConfigurationProperties("security.space")
@Data
public class SpaceProperties {

    /**
     * 是否开启去除空格
     */
    private Boolean enabled = true;

    /**
     * 放行拦截的路径
     */
    private Set<String> skipUrl = new HashSet<>();


}