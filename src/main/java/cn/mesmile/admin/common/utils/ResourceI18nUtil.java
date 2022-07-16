package cn.mesmile.admin.common.utils;

import cn.hutool.extra.spring.SpringUtil;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceResolvable;
import org.springframework.context.i18n.LocaleContextHolder;

/**
 * @author zb
 * @Description
 */
public class ResourceI18nUtil {

    public static MessageSource getMessageSource() {
        return SpringUtil.getBean(MessageSource.class);
    }

    /**
     * 通过定义key 获取value
     * @param key key
     * @return value
     */
    public static String getValueByKey(String key){
        return getMessageSource().getMessage(key, null, LocaleContextHolder.getLocale());
    }

    /**
     * 通过定义key 获取value
     * @param key key
     * @param args 填补参数
     * @return value
     */
    public static String getValueByKey(String key, Object... args){
        return getMessageSource().getMessage(key, args, LocaleContextHolder.getLocale());
    }

    /**
     * 通过定义key 获取value
     * @param key key
     * @param defaultMessage 默认消息
     * @param args 填补参数
     * @return value
     */
    public static String getValueDefaultByKey(String key,String defaultMessage,Object... args){
        return getMessageSource().getMessage(key, args, defaultMessage, LocaleContextHolder.getLocale());
    }

    /**
     * 通过定义key 获取value
     * @param key key
     * @param defaultMessage 默认消息
     * @return value
     */
    public static String getValueDefaultByKey(String key,String defaultMessage){
        return getMessageSource().getMessage(key, null, defaultMessage,LocaleContextHolder.getLocale());
    }

    /**
     * 通过定义key 获取value
     * @param resolvable resolvable
     * @return value
     */
    public static String getValueByResolvable(MessageSourceResolvable resolvable){
        return getMessageSource().getMessage(resolvable, LocaleContextHolder.getLocale());
    }

}
