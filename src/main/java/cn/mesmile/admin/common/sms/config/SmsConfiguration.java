package cn.mesmile.admin.common.sms.config;

import cn.mesmile.admin.common.exceptions.ServiceException;
import cn.mesmile.admin.common.sms.AliSmsTemplate;
import cn.mesmile.admin.common.sms.config.SmsProperties;
import com.aliyun.dysmsapi20170525.Client;
import com.aliyun.teaopenapi.models.Config;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author zb
 * @Description
 */
@EnableConfigurationProperties({SmsProperties.class})
@Configuration
@ConditionalOnProperty(value = {"sms.enabled"}, havingValue = "true")
public class SmsConfiguration {

    @Bean
    @ConditionalOnProperty(value = {"sms.platform-type"}, havingValue = "ali_yun")
    public AliSmsTemplate getAliSmsTemplate(SmsProperties smsProperties) {
        Config config = new Config()
                // 您的 AccessKey ID
                .setAccessKeyId(smsProperties.getAccessKey())
                // 您的 AccessKey Secret
                .setAccessKeySecret(smsProperties.getSecretKey());
        config.endpoint = "dysmsapi.aliyuncs.com";
        com.aliyun.dysmsapi20170525.Client client;
        try {
            client = new Client(config);
        }catch (Exception e){
            throw new ServiceException("创建阿里云短信客户端失败", e);
        }
        return new AliSmsTemplate(smsProperties, client);
    }

//    @Bean
//    @ConditionalOnProperty(value = {"sms.platform-type"}, havingValue = "tencent_yun")
//    public TencentSmsTemplate tencentSmsTemplate(SmsProperties smsProperties) {
//        SmsMultiSender smsSender = new SmsMultiSender(smsProperties.getAccessKey(), smsProperties.getSecretKey());
//        return new TencentSmsTemplate(smsProperties, smsSender);
//    }


}
