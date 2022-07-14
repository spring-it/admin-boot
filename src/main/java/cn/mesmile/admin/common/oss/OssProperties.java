package cn.mesmile.admin.common.oss;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

/**
 * @author zb
 * @Description oss 配置
 */
@Data
@EnableConfigurationProperties({OssProperties.class})
@ConfigurationProperties(prefix = "oss")
public class OssProperties {

    /**
     * 是否启用 Oss 文件存储
     */
    private Boolean enabled;

    /**
     * 文件存储系统类型
     */
    private String type;

    /**
     * oss对外开放的地址
     */
    private String endpoint;

    /**
     * accessKey
     */
    private String accessKey;

    /**
     * secretKey
     */
    private String secretKey;

    /**
     * 桶名称
     */
    private String bucketName = "resource";
}
