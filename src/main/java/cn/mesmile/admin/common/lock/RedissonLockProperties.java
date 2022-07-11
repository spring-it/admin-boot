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

    public static final String PREFIX = "redisson.lock";
    private Boolean enabled;
    private String address;
    private String password;
    private Integer database;
    private Integer poolSize;
    private Integer idleSize;
    private Integer idleTimeout;
    private Integer connectionTimeout;
    private Integer timeout;
    private RedissonLockProperties.Mode mode;
    private String masterAddress;
    private String[] slaveAddress;
    private String masterName;
    private String[] sentinelAddress;
    private String[] nodeAddress;

    public RedissonLockProperties() {
        this.enabled = Boolean.FALSE;
        this.address = "redis://127.0.0.1:6379";
        this.database = 0;
        this.poolSize = 20;
        this.idleSize = 5;
        this.idleTimeout = 60000;
        this.connectionTimeout = 3000;
        this.timeout = 10000;
        this.mode = RedissonLockProperties.Mode.single;
    }


    public static enum Mode {
        /**
         * 不同模式
         */
        single,
        master,
        sentinel,
        cluster;
        private Mode() {
        }
    }
}