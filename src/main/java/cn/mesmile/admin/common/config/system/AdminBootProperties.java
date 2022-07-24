package cn.mesmile.admin.common.config.system;

import cn.mesmile.admin.modules.auth.enums.PasswordEncoderTypeEnum;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

/**
 * @author zb
 * @Description 系统配置信息
 */
@ConfigurationProperties(prefix = "admin-boot")
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

    /**
     * 密码加密方式
     */
    private PasswordEncoderTypeEnum passwordEncoderType = PasswordEncoderTypeEnum.BCRYPT;

}
