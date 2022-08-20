package cn.mesmile.admin.common.desensitization;

/**
 * @author zb
 * @Description 脱敏数据字段类型
 */
public enum  PrivacyTypeEnum {

    /** 自定义（此项需设置脱敏的范围）*/
    CUSTOMER,

    /** 姓名 */
    NAME,

    /** 身份证号 */
    ID_CARD,

    /** 手机号 */
    PHONE,

    /** 邮箱 */
    EMAIL

}
