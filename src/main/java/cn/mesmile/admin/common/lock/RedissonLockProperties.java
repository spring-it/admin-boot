package cn.mesmile.admin.common.lock;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author zb
 * @Description
 */
@Data
@ConfigurationProperties("redisson.lock")
public class RedissonLockProperties {

    /**
     * 是否开启 redisson 分布式锁，默认 false
     */
    private Boolean enabled;
    /**
     * redis 地址，默认 redis://127.0.0.1:6379
     */
    private String address;
    /**
     * redis 密码
     */
    private String password;
    /**
     * 数据库下标
     */
    private Integer database;
    /**
     * 连接池大小
     */
    private Integer poolSize;
    /**
     * 最小空闲连接数
     */
    private Integer idleSize;
    /**
     * 连接空闲超时时间，单位 毫秒
     * 如果池连接在timeout时间内未使用并且当前连接量大于最小空闲连接池大小，
     * 则它将关闭并从池中删除。
     */
    private Integer idleTimeout;
    /**
     * 连接超时时间，单位 毫秒
     */
    private Integer connectionTimeout;
    /**
     * 执行命令等待超时时间，单位 毫秒
     * Redis 服务器响应超时。 Redis 命令发送成功后开始倒计时
     */
    private Integer timeout;
    /**
     * redisson 连接redis 的模式
     */
    private RedissonLockProperties.Mode mode;

    /**
     * 主从模式配置
     */
    private String masterAddress;
    private String[] slaveAddress;

    /**
     * 哨兵模式配置
     */
    private String masterName;
    /** 一个或多个哨兵地址和端口号 [ip:port] */
    private String[] sentinelAddress;

    /**
     * 集群模式配置
     * 一个或多个集群节点地址和端口号 [ip:port]
     */
    private String[] nodeAddress;

    public RedissonLockProperties() {
        this.enabled = Boolean.FALSE;
        this.address = "redis://127.0.0.1:6379";
        this.database = 0;
        this.poolSize = 30;
        this.idleSize = 10;
        this.idleTimeout = 60000;
        this.connectionTimeout = 3000;
        this.timeout = 10000;
        this.mode = RedissonLockProperties.Mode.single;
    }


    public static enum Mode {
        /**
         * redis不同部署模式
         * single 单机
         * master 主从
         * sentinel 哨兵
         * cluster 集群
         */
        single,
        master,
        sentinel,
        cluster;
    }
}