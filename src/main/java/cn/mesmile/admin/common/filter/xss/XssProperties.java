package cn.mesmile.admin.common.filter.xss;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.HashSet;
import java.util.Set;

/**
 * 预防xss攻击设置
 *
 * @author zb
 * @Description
 */
@ConfigurationProperties("security.xss")
@Data
public class XssProperties {

    /**
     * 是否开启xss拦截
     */
    private Boolean enabled = true;

    /**
     * 放行拦截的路径
     */
    private Set<String> skipUrl = new HashSet<>();

}