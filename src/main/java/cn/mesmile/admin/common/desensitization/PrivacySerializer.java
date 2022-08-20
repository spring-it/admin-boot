package cn.mesmile.admin.common.desensitization;

import cn.mesmile.admin.common.exceptions.ServiceException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.util.Objects;

/**
 * @author zb
 * @Description aop的方式序列化的时候进行脱敏
 *      如果在保存数据的时候使用此注解的类会把处理好的数据保存到数据库从而导致数据不准确
 *      有时候查询的数据需要部署脱敏的数据而是原数据 解决：可以多声明一个类的字段，
 *      如idCardNumber2代表脱敏数据，而idCardNumber代表原数据即可
 */
@NoArgsConstructor
@AllArgsConstructor
public class PrivacySerializer extends JsonSerializer<String> implements ContextualSerializer {

    /** 脱敏类型 */
    private PrivacyTypeEnum privacyTypeEnum;
    /** 前几位不脱敏 */
    private Integer prefixNoMaskLen;
    /** 最后几位不脱敏 */
    private Integer suffixNoMaskLen;
    /** 用什么打码 */
    private String symbol;

    @Override
    public void serialize(final String origin, final JsonGenerator jsonGenerator,
                          final SerializerProvider serializerProvider) throws IOException {
        if (StringUtils.isNotBlank(origin) && null != privacyTypeEnum) {
            switch (privacyTypeEnum) {
                case CUSTOMER:
                    jsonGenerator.writeString(PrivacyUtil.desValue(origin, prefixNoMaskLen, suffixNoMaskLen, symbol));
                    break;
                case NAME:
                    jsonGenerator.writeString(PrivacyUtil.hideChineseName(origin));
                    break;
                case ID_CARD:
                    jsonGenerator.writeString(PrivacyUtil.hideIDCard(origin));
                    break;
                case PHONE:
                    jsonGenerator.writeString(PrivacyUtil.hidePhone(origin));
                    break;
                case EMAIL:
                    jsonGenerator.writeString(PrivacyUtil.hideEmail(origin));
                    break;
                default:
                    throw new ServiceException("unknown privacy type enum " + privacyTypeEnum);
            }
        }
    }

    @Override
    public JsonSerializer<?> createContextual(final SerializerProvider serializerProvider,
                                              final BeanProperty beanProperty) throws JsonMappingException {
        if (beanProperty != null) {
            if (Objects.equals(beanProperty.getType().getRawClass(), String.class)) {
                PrivacyEncrypt privacyEncrypt = beanProperty.getAnnotation(PrivacyEncrypt.class);
                if (privacyEncrypt == null) {
                    privacyEncrypt = beanProperty.getContextAnnotation(PrivacyEncrypt.class);
                }
                if (privacyEncrypt != null) {
                    return new PrivacySerializer(privacyEncrypt.type(), privacyEncrypt.prefixNoMaskLen(),
                            privacyEncrypt.suffixNoMaskLen(), privacyEncrypt.symbol());
                }
            }
            return serializerProvider.findValueSerializer(beanProperty.getType(), beanProperty);
        }
        return serializerProvider.findNullValueSerializer(null);
    }
}
