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
