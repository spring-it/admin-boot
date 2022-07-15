package cn.mesmile.admin.common.oss;

import cn.mesmile.admin.common.oss.enums.OssTypeEnum;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author zb
 * @Description oss 配置
 */
@Data
@ConfigurationProperties(prefix = "oss")
public class OssProperties {

    /**
     * 是否启用 Oss 文件存储
     */
    private Boolean enabled = Boolean.FALSE;

    /**
     * 文件存储系统类型
     */
    private OssTypeEnum type;

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
