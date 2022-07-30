package cn.mesmile.admin.common.sms.config;

import cn.mesmile.admin.common.sms.domain.SmsPlatformTypeEnum;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author zb
 * @Description
 */
@Data
@Component
@ConfigurationProperties(prefix = "sms")
public class SmsProperties {
    /**
     * 是否开启短信模块
     */
    private Boolean enabled = Boolean.FALSE;
    /**
     * 短信平台类型
     */
    private SmsPlatformTypeEnum platformType  = SmsPlatformTypeEnum.ALI_YUN;
    /**
     * 短信模板id
     */
    private String templateId;
    /**
     * 地域id
     */
    private String regionId = "cn-hangzhou";
    /**
     * accessKey
     */
    private String accessKey;
    /**
     * secretKey
     */
    private String secretKey;
    /**
     * 短信签名
     */
    private String signName;

}