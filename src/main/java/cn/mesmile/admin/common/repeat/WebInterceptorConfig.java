package cn.mesmile.admin.common.repeat;

import cn.mesmile.admin.common.log.RequestLogInterceptor;
import cn.mesmile.admin.common.utils.AdminRedisTemplate;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author zb
 * @Description
 */
@EnableConfigurationProperties({RepeatSubmitProperties.class})
@Configuration(
        proxyBeanMethods = false
)
public class WebInterceptorConfig implements WebMvcConfigurer {

    @Resource
    private RepeatSubmitProperties repeatSubmitProperties;

    @Resource
    private AdminRedisTemplate adminRedisTemplate;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 日志请求id
        registry.addInterceptor(new RequestLogInterceptor()).addPathPatterns("/**");
        Boolean enabled = repeatSubmitProperties.getEnabled();
        if (enabled){
            // 重复提交拦截
            RepeatSubmitInterceptor repeatSubmitInterceptor = new RepeatSubmitInterceptor(adminRedisTemplate);
            List<String> skipUrl = repeatSubmitProperties.getSkipUrl();
            // 注册过滤器，并且放行某些路径
            registry.addInterceptor(repeatSubmitInterceptor).excludePathPatterns(skipUrl);
        }
    }
}