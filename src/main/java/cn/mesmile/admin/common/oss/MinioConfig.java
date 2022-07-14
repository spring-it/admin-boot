package cn.mesmile.admin.common.oss;

import io.minio.MinioClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

/**
 * @author zb
 * @Description
 */
@EnableConfigurationProperties({OssProperties.class})
@Configuration(proxyBeanMethods = false)
@ConditionalOnProperty(
        value = {"oss.enabled"},
        havingValue = "true"
)
public class MinioConfig {

    @Resource
    private OssProperties ossProperties;

    @Bean
    @ConditionalOnMissingBean({MinioClient.class})
    @ConditionalOnProperty(
            value = {"oss.type"},
            havingValue = "minio_oss"
    )
    public MinioClient minioClient() {
        return MinioClient.builder()
                .endpoint(ossProperties.getEndpoint())
                .credentials(ossProperties.getAccessKey(), ossProperties.getSecretKey())
                .build();
    }

    @Bean
    @ConditionalOnBean({MinioClient.class})
    @ConditionalOnMissingBean({MinioTemplate.class})
    public MinioTemplate minioTemplate(MinioClient minioClient) {
        return new MinioTemplate(new OssRuleImpl(), minioClient, ossProperties);
    }


}
