package cn.mesmile.admin.modules.scheduled;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author zb
 * @Description
 *   官网: https://www.xuxueli.com/xxl-job/
 */
@Data
@ConfigurationProperties(prefix = "xxl.job")
public class XxlJobProperties {

    /**
     * 是否开启xxl-job ,默认不开启
     */
    private Boolean enabled = Boolean.FALSE;

    /**
     * 调度中心部署根地址 [选填]：如调度中心集群部署存在多个地址则用逗号分隔
     */
    private String serviceAddresses;
    /**
     * 执行器通讯TOKEN [选填]：非空时启用
     */
    private String accessToken;

    /**
     * 执行器AppName [选填]：执行器心跳注册分组依据；为空则关闭自动注册
     * 扩展：这里可以配置为 map -> 不同的服务器ip 用不同的 appname
     */
    private String appName;

    /**
     * 执行器IP [选填]：默认为空表示自动获取IP，多网卡时可手动设置指定IP，该IP不会绑定Host仅作为通讯实用
     */
    private String clientAddress;

    /**
     * 执行器IP [选填]：默认为空表示自动获取IP，多网卡时可手动设置指定IP，该IP不会绑定Host仅作为通讯实用
     */
    private String clientIp;

    /**
     * 执行器IP [选填]：默认为空表示自动获取IP，多网卡时可手动设置指定IP，该IP不会绑定Host仅作为通讯实用
     */
    private Integer clientPort;

    /**
     * 执行器运行日志文件存储磁盘路径 [选填] ：需要对该路径拥有读写权限；为空则使用默认路径；
     */
    private String clientLogPath;

    /**
     * 执行器运行日志文件存储磁盘路径 [选填] ：需要对该路径拥有读写权限；为空则使用默认路径；
     */
    private Integer clientLogRetentionDays;

}

/*
 * 针对多网卡、容器内部署等情况，可借助 "spring-cloud-commons" 提供的 "InetUtils" 组件灵活定制注册IP；
 *
 *      1、引入依赖：
 *          <dependency>
 *             <groupId>org.springframework.cloud</groupId>
 *             <artifactId>spring-cloud-commons</artifactId>
 *             <version>${version}</version>
 *         </dependency>
 *
 *      2、配置文件，或者容器启动变量
 *          spring.cloud.inetutils.preferred-networks: 'xxx.xxx.xxx.'
 *
 *      3、获取IP
 *          String ip_ = inetUtils.findFirstNonLoopbackHostInfo().getIpAddress();
 */

/*
### 调度中心部署根地址 [选填]：如调度中心集群部署存在多个地址则用逗号分隔。执行器将会使用该地址进行"执行器心跳注册"和"任务结果回调"；为空则关闭自动注册；
xxl.job.admin.addresses=http://127.0.0.1:8080/xxl-job-admin
### 执行器通讯TOKEN [选填]：非空时启用；
xxl.job.accessToken=
### 执行器AppName [选填]：执行器心跳注册分组依据；为空则关闭自动注册
xxl.job.executor.appname=xxl-job-executor-sample
### 执行器注册 [选填]：优先使用该配置作为注册地址，为空时使用内嵌服务 ”IP:PORT“ 作为注册地址。从而更灵活的支持容器类型执行器动态IP和动态映射端口问题。
xxl.job.executor.address=
### 执行器IP [选填]：默认为空表示自动获取IP，多网卡时可手动设置指定IP，该IP不会绑定Host仅作为通讯实用；地址信息用于 "执行器注册" 和 "调度中心请求并触发任务"；
xxl.job.executor.ip=
### 执行器端口号 [选填]：小于等于0则自动获取；默认端口为9999，单机部署多个执行器时，注意要配置不同执行器端口；
xxl.job.executor.port=9999
### 执行器运行日志文件存储磁盘路径 [选填] ：需要对该路径拥有读写权限；为空则使用默认路径；
xxl.job.executor.logpath=/data/applogs/xxl-job/jobhandler
### 执行器日志文件保存天数 [选填] ： 过期日志自动清理, 限制值大于等于3时生效; 否则, 如-1, 关闭自动清理功能；
xxl.job.executor.logretentiondays=30
 */