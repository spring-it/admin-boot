package cn.mesmile.admin.common.sms;

import cn.mesmile.admin.common.sms.domain.SmsCode;
import cn.mesmile.admin.common.sms.domain.SmsData;
import cn.mesmile.admin.common.sms.domain.SmsResponse;
import lombok.extern.slf4j.Slf4j;

import java.util.Collection;

/**
 * @author zb
 * @Description
 */
@Slf4j
public class TencentSmsTemplate implements SmsTemplate {

//    private final SmsProperties smsProperties;
//    private final Client aliSmsClient;

    @Override
    public SmsResponse sendSingleMessage(SmsData smsData, String phones) {

        return null;
    }

    @Override
    public SmsResponse sendMessage(SmsData smsData, Collection<String> phones) {
        return null;
    }

    @Override
    public SmsCode sendValidate(SmsData smsData, String phone) {
        return null;
//        SmsCode smsCode = new SmsCode();
//        boolean temp = this.sendSingle(smsData, phone);
//        if (temp && StringUtil.isNotBlank(smsData.getKey())) {
//            String id = StringUtil.randomUUID();
//            String value = (String)smsData.getParams().get(smsData.getKey());
//            this.redis.setEx("blade:sms::captcha:" + phone + ":" + id, value, Duration.ofMinutes(30L));
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
//        String cache = (String)this.redis.get("blade:sms::captcha:" + smsCode.getPhone() + ":" + id);
//        if (StringUtil.isNotBlank(value) && StringUtil.equalsIgnoreCase(cache, value)) {
//            this.redis.del("blade:sms::captcha:" + id);
//            return true;
//        } else {
//            return false;
//        }
    }

//    public TencentSmsTemplate(final SmsProperties smsProperties, final Client aliSmsClient) {
//        this.smsProperties = smsProperties;
//        this.aliSmsClient = aliSmsClient;
//    }

}
