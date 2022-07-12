package cn.mesmile.admin.common.limit;

/**
 * @author zb
 * @Description 限制频率的限制模式
 */
public enum  LimiterModeEnum {

    /**
     * 限制频率的限制模式
     *  LIMITER_ALL 同一个接口，针对所有ip请求限制
     *  LIMITER_IP  同一个接口，针对单个ip请求限制
     */
    LIMITER_ALL,
    LIMITER_IP

}
