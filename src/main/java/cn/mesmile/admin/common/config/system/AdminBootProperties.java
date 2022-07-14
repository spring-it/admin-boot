package cn.mesmile.admin.common.config.system;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

/**
 * @author zb
 * @Description 系统配置信息
 */
@EnableConfigurationProperties({AdminBootProperties.class})
@ConfigurationProperties("admin-boot")
@Data
public class AdminBootProperties {

    /**
     *  系统版本
     */
    private String version;

    /**
     *  系统信息
     */
    private String info = "Copyright 2022 - 2032 Admin-Boot. All Rights Reserved";

}
