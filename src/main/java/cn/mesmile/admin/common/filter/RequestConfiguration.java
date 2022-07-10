package cn.mesmile.admin.common.filter;

import cn.mesmile.admin.common.filter.space.SpaceProperties;
import cn.mesmile.admin.common.filter.space.SpaceRequestFilter;
import cn.mesmile.admin.common.filter.xss.XssProperties;
import cn.mesmile.admin.common.filter.xss.XssRequestFilter;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.DispatcherType;

/**
 * @author zb
 * @Description 配置注册过滤器
 */
@Configuration(
        proxyBeanMethods = false
)
@EnableConfigurationProperties({XssProperties.class, SpaceProperties.class})
public class RequestConfiguration {

    private final SpaceProperties spaceProperties;
    private final XssProperties xssProperties;

    @Bean
    public FilterRegistrationBean<XssRequestFilter> xssFilterRegistration() {
        FilterRegistrationBean<XssRequestFilter> registration = new FilterRegistrationBean();
        registration.setDispatcherTypes(DispatcherType.REQUEST, new DispatcherType[0]);
        registration.setFilter(new XssRequestFilter(this.xssProperties));
        registration.addUrlPatterns(new String[]{"/*"});
        registration.setName("xssRequestFilter");
        registration.setOrder(Integer.MAX_VALUE);
        return registration;
    }

    @Bean
    public FilterRegistrationBean<SpaceRequestFilter> spaceFilterRegistration() {
        FilterRegistrationBean<SpaceRequestFilter> registration = new FilterRegistrationBean();
        registration.setDispatcherTypes(DispatcherType.REQUEST, new DispatcherType[0]);
        registration.setFilter(new SpaceRequestFilter(this.spaceProperties));
        registration.addUrlPatterns(new String[]{"/*"});
        registration.setName("spaceRequestFilter");
        registration.setOrder(Integer.MAX_VALUE - 1);
        return registration;
    }

    public RequestConfiguration(final XssProperties xssProperties, final SpaceProperties spaceProperties) {
        this.xssProperties = xssProperties;
        this.spaceProperties = spaceProperties;
    }

}
