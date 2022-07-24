package cn.mesmile.admin.modules.auth.enums;

/**
 * @author zb
 * @Description 密码加密方式
 */
public enum  PasswordEncoderTypeEnum {

    /**
     * 密码加密方式
     */
    BCRYPT("bcrypt","{bcrypt}"),
    PBKDF2("pbkdf2","{pbkdf2}"),
    SCRYPT("scrypt","{scrypt}"),
    ARGON2("argon2","{argon2}"),

    /** ldap不安全，不建议使用*/
    LDAP("ldap","{ldap}"),
    /** MD4不安全，不建议使用*/
    MD4("MD4","{MD4}"),
    /** MD5不安全，不建议使用*/
    MD5("MD5","{MD5}"),
    /** noop不安全，不建议使用*/
    NOOP("noop","{noop}"),
    /** SHA-1不安全，不建议使用*/
    SHA_1("SHA-1","{SHA-1}"),
    /** SHA-256不安全，不建议使用*/
    SHA_256("SHA-256","{SHA-256}"),
    /** sha256不安全，不建议使用*/
    SHA256("sha256","{sha256}");

     /*
        存储密码格式：{加密方式}加密密码
        {bcrypt}$2a$10$dXJ3SW6G7P50lGmMkkmwe.20cQQubK3.HZWzG3YB1tlRy.fqvM/BG
        {noop}password
        {pbkdf2}5d923b44a6d129f3ddf3e3c8d29412723dcbde72445e8ef6bf3b508fbf17fa4ed4d6b99ca763d8dc
        {scrypt}$e0801$8bWJaSu2IKSn9Z9kM+TPXfOc/9bdYSrN1oD9qfVThWEwdRTnO7re7Ei+fUZRJ68k9lTyuTeUp4of4g24hHnazw==$OAOec05+bXxvuu/1qZ6NUR+xQYvYv7BeL1QxwRpY5Pc=
        {sha256}97cde38028ad898ebc02e690819fa220e88c62e0699403e94fff291cfffaf8410849f27605abcbc0
     */
    /**
     * 加密方式id
     */
    String idForEncode;
    /**
     * 存储在数据库中 加密密码的前缀
     */
    String storePasswordPrefix;

    PasswordEncoderTypeEnum(String idForEncode,String storePasswordPrefix){
        this.idForEncode = idForEncode;
        this.storePasswordPrefix = storePasswordPrefix;
    }

    public String getIdForEncode(){
        return idForEncode;
    }

    public String getStorePasswordPrefix(){
        return storePasswordPrefix;
    }
}
