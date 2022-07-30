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