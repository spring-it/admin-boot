package cn.mesmile.admin.common.captcha;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author zb
 * @Description
 */
@Component
@Data
@ConfigurationProperties(prefix = "captcha")
public class CaptchaProperties {

    /**
     * 是否开启验证码
     */
    private Boolean enabled = Boolean.FALSE;

    /**
     * 验证码模式
     */
    private VerifyTypeEnum verifyType = VerifyTypeEnum.CALCULATE;

    /**
     * 过期时间，单位 秒
     */
    private Integer expire = 120;
}
