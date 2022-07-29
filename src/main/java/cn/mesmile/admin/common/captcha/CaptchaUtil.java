package cn.mesmile.admin.common.captcha;

import cn.hutool.core.util.StrUtil;
import cn.mesmile.admin.common.constant.AdminConstant;
import cn.mesmile.admin.common.exceptions.ServiceException;
import cn.mesmile.admin.common.utils.AdminRedisTemplate;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.google.code.kaptcha.Producer;
import org.springframework.util.FastByteArrayOutputStream;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.time.Duration;
import java.util.Base64;

/**
 * @author zb
 * @Description
 */
public class CaptchaUtil {

    private CaptchaUtil(){}

    /**
     * 检查验证码是否有效
     * @param uuid 唯一键值
     * @param codeAnswer 验证码答案
     * @param adminRedisTemplate redis工具类
     * @return 验证成功为 TRUE  验证失败为 FALSE
     */
    public static boolean checkVerificationCode(String uuid, String codeAnswer,AdminRedisTemplate adminRedisTemplate){
        String verifyKey = AdminConstant.CAPTCHA_CODE_KEY + uuid;
        String answer = adminRedisTemplate.get(verifyKey);
        if (StrUtil.isNotEmpty(codeAnswer) && answer != null && answer.equals(codeAnswer)){
            return true;
        }
        return false;
    }

    /**
     * 获取Base64图片
     * @param captchaProducerMath 图片生成器
     * @param adminRedisTemplate redis工具类
     * @param captchaProperties 配置
     * @param uuid 唯一值
     * @return base64字符串图片
     */
    public static String getImageBase64Str(Producer captchaProducerMath, AdminRedisTemplate adminRedisTemplate,
                                           CaptchaProperties captchaProperties,String uuid){
        BufferedImage imageStream = getImageStream(captchaProducerMath, adminRedisTemplate, captchaProperties, uuid);
        try (
                FastByteArrayOutputStream outputStream = new FastByteArrayOutputStream();
                ){
            ImageIO.write(imageStream, "jpg", outputStream);
            return Base64.getEncoder().encodeToString(outputStream.toByteArray());
        }catch (Exception e) {
           throw new ServiceException("生成验证码异常", e);
        }
    }

    public static BufferedImage getImageStream(Producer captchaProducerMath, AdminRedisTemplate adminRedisTemplate,
                                        CaptchaProperties captchaProperties,String uuid){
        String verifyKey = AdminConstant.CAPTCHA_CODE_KEY + uuid;
        // 创造图形
        String capText = captchaProducerMath.createText();
        String capStr = null;
        String answer = null;
        if (captchaProperties.getVerifyType().equals(VerifyTypeEnum.CALCULATE)){
            capStr = capText.substring(0, capText.lastIndexOf(StringPool.AT));
            answer = capText.substring(capText.lastIndexOf(StringPool.AT) + 1);
        }else if (captchaProperties.getVerifyType().equals(VerifyTypeEnum.RANDOM_LETTER_NUMBER)){
            capStr = capText;
            answer = capText;
        }
        BufferedImage image = captchaProducerMath.createImage(capStr);
        // 验证码默认有效期为两分钟
        adminRedisTemplate.setEx(verifyKey, answer, Duration.ofSeconds(captchaProperties.getExpire()));
        return image;
    }

}
