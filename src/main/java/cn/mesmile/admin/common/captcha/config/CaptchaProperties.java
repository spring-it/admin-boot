package cn.mesmile.admin.common.captcha.config;

/**
 * @author zb
 * @Description
 */
public class CaptchaProperties {

    /**
     * 是否开启验证码
     */
    private Boolean enabled = Boolean.FALSE;

    /**
     * 验证码模式
     */
    private VerifyTypeEnum verifyType = VerifyTypeEnum.CALCULATE;
}
