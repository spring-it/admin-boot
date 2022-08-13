package cn.mesmile.admin.modules.message;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author zb
 * @Description
 */
@Data
@ConfigurationProperties(prefix = "admin.message")
public class MessageProperties {

    /**
     * 是否启用消息发送
     */
    private Boolean enabled = Boolean.FALSE;

    /**
     * 发送消息底层调用逻辑
     */
    private MessageServiceTypeEnum serviceType = MessageServiceTypeEnum.RABBIT_MQ;

    /**
     * mqtt配置 服务端ip
     */
    private String mqttServerIp = "0.0.0.0";

    /**
     * mqtt服务端 端口号
     */
    private Integer mqttServerPort = 1883;

    /**
     * webSocket端口号
     */
    private Integer mqttServerWebsocketPort = 5883;

}
