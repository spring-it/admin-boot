# (二十四)集成sms短信服务

## 目录

*   [一、编码实现](#一编码实现)

    *   [SmsProperties 配置字段属性](#smsproperties-配置字段属性)

    *   [SmsConfiguration 配置类](#smsconfiguration-配置类)

    *   [SmsTemplate 顶层短信接口](#smstemplate-顶层短信接口)

    *   [AliSmsTemplate 具体实现模板](#alismstemplate-具体实现模板)

## 一、编码实现

**短信模块所在路径 cn.mesmile.admin.common.sms**

### SmsProperties 配置字段属性

```java
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
```

### SmsConfiguration 配置类

```java
package cn.mesmile.admin.common.sms.config;

import cn.mesmile.admin.common.exceptions.ServiceException;
import cn.mesmile.admin.common.sms.AliSmsTemplate;
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

```

### SmsTemplate 顶层短信接口

```java
package cn.mesmile.admin.common.sms;

import cn.mesmile.admin.common.sms.domain.SmsCode;
import cn.mesmile.admin.common.sms.domain.SmsData;
import cn.mesmile.admin.common.sms.domain.SmsResponse;

import java.util.Collection;

/**
 * @author zb
 * @Description
 */
public interface SmsTemplate {

    /**
     * 发送单条消息
     * @param smsData 短信内容匹配
     * @param phones 电话号码
     * @return 发送结果
     */
    SmsResponse sendSingleMessage(SmsData smsData, String phones);
    /**
     * 批量发送消息
     * @param smsData 短信内容匹配
     * @param phones 多个电话号码
     * @return 发送结果
     */
    SmsResponse sendMessage(SmsData smsData, Collection<String> phones);

    /**
     *  发送验证码
     * @param smsData  短信内容匹配
     * @param phone 电话号码
     * @return 发送结果
     */
    SmsCode sendValidate(SmsData smsData, String phone);

    /**
     * 校验消息
     * @param smsCode
     * @return
     */
    boolean validateMessage(SmsCode smsCode);
}
```

### AliSmsTemplate 具体实现模板

```java
package cn.mesmile.admin.common.sms;

import cn.mesmile.admin.common.exceptions.ServiceException;
import cn.mesmile.admin.common.result.ResultCode;
import cn.mesmile.admin.common.sms.config.SmsProperties;
import cn.mesmile.admin.common.sms.domain.SmsCode;
import cn.mesmile.admin.common.sms.domain.SmsData;
import cn.mesmile.admin.common.sms.domain.SmsResponse;
import com.alibaba.fastjson.JSONObject;
import com.aliyun.dysmsapi20170525.models.SendSmsRequest;
import com.aliyun.dysmsapi20170525.models.SendSmsResponse;
import com.aliyun.tea.TeaException;
import com.aliyun.teautil.Common;
import com.aliyun.teautil.models.RuntimeOptions;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;

import java.util.Collection;

/**
 * @author zb
 * @Description
 */
@Slf4j
public class AliSmsTemplate implements SmsTemplate {

    private final SmsProperties smsProperties;
    private final com.aliyun.dysmsapi20170525.Client aliSmsClient;

    @Override
    public SmsResponse sendSingleMessage(SmsData smsData, String phones) {
        SendSmsRequest sendSmsRequest = new SendSmsRequest();
        sendSmsRequest.setSignName(smsProperties.getSignName());
        sendSmsRequest.setTemplateCode(smsProperties.getTemplateId());
        sendSmsRequest.setPhoneNumbers(phones);
        sendSmsRequest.setTemplateParam(JSONObject.toJSONString(smsData.getParams()));
        RuntimeOptions runtime = new RuntimeOptions();
        try {

            SendSmsResponse sendSmsResponse = aliSmsClient.sendSmsWithOptions(sendSmsRequest, runtime);
            return new SmsResponse(sendSmsResponse.getStatusCode() == HttpStatus.OK.value(),
                    HttpStatus.OK.value(), JSONObject.toJSONString(sendSmsResponse.getBody()));
        } catch (TeaException error) {
            String msg = Common.assertAsString(error.message);
            throw new ServiceException(ResultCode.FAILURE, msg, error);
        } catch (Exception e) {
            TeaException error = new TeaException(e.getMessage(), e);
            String msg = Common.assertAsString(error.message);
            throw new ServiceException(ResultCode.FAILURE, msg, error);
        }
    }

    @Override
    public SmsResponse sendMessage(SmsData smsData, Collection<String> phones) {
      return this.sendSingleMessage(smsData, String.join(",", phones));
    }

    @Override
    public SmsCode sendValidate(SmsData smsData, String phone) {
        return null;
//        SmsCode smsCode = new SmsCode();
//        boolean temp = this.sendSingle(smsData, phone);
//        if (temp && StringUtil.isNotBlank(smsData.getKey())) {
//            String id = StringUtil.randomUUID();
//            String value = (String)smsData.getParams().get(smsData.getKey());
//            this.redis.setEx("admin:sms::captcha:" + phone + ":" + id, value, Duration.ofMinutes(30L));
//            smsCode.setId(id).setValue(value);
//        } else {
//            smsCode.setSuccess(Boolean.FALSE);
//        }
//        return smsCode;
    }

    @Override
    public boolean validateMessage(SmsCode smsCode) {
        return true;
//        String id = smsCode.getId();
//        String value = smsCode.getValue();
//        String cache = (String)this.redis.get("admin:sms::captcha:" + smsCode.getPhone() + ":" + id);
//        if (StringUtil.isNotBlank(value) && StringUtil.equalsIgnoreCase(cache, value)) {
//            this.redis.del("admin:sms::captcha:" + id);
//            return true;
//        } else {
//            return false;
//        }
    }

    public AliSmsTemplate(final SmsProperties smsProperties, final com.aliyun.dysmsapi20170525.Client aliSmsClient) {
        this.smsProperties = smsProperties;
        this.aliSmsClient = aliSmsClient;
    }

}

```

引入maven依赖

```xml
<!--AliSms 具体的短信平台，引入的依赖不同-->
<dependency>
    <groupId>com.aliyun</groupId>
    <artifactId>dysmsapi20170525</artifactId>
    <version>2.0.16</version>
</dependency>
```
