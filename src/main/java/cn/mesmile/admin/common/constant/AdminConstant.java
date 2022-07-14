package cn.mesmile.admin.common.constant;

/**
 * @author zb
 * @Description
 */
public interface AdminConstant {

    /**
     * 基础包路径
     */
    String BASE_PACKAGE = "cn.mesmile";

    /**
     * redis分布式所的默认前缀
     */
    String REDIS_LOCK_PREFIX = "redis_lock";

    /**
     * 限流key的默认前缀
     */
    String RATE_LIMITER_PREFIX = "rate_limiter";

    /**
     * 重复提交key的默认前缀
     */
    String REPEAT_SUBMIT_PREFIX = "repeat_submit";

}
