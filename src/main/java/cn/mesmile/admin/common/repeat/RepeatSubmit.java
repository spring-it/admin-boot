package cn.mesmile.admin.common.repeat;

import cn.mesmile.admin.common.constant.AdminConstant;

import java.lang.annotation.*;
import java.util.concurrent.TimeUnit;

/**
 * @author zb
 * @Description 检查重复提交
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RepeatSubmit {

    /**
     * 间隔时间，小于此时间视为重复提交
     */
    long interval() default 5000;

    /**
     * 默认时间单位 毫秒
     */
    TimeUnit timeUnit() default TimeUnit.MILLISECONDS;

    /**
     * 自定义判断重复的标识 支持 spring el 例如  #user.getName()
     * 当有多个表达式的时候中间用 ; 分割
     * 当此参数不为空的时候，就根据此处表达式获取的值来判断是否重复提交
     */
    String param() default "";

    /**
     * 默认缓存key的前缀
     */
    String prefix() default AdminConstant.REPEAT_SUBMIT_PREFIX;

    /**
     * 提示消息
     */
    String msg() default "系统正在处理，请勿重复提交";

}