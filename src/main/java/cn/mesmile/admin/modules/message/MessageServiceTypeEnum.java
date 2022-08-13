package cn.mesmile.admin.modules.message;

/**
 * @author zb
 * @Description 发送消息调用的底层服务
 */
public enum  MessageServiceTypeEnum {

    /**
     * rabbitMq
     */
    RABBIT_MQ,
    /**
     * rocketMq
     */
    ROCKET_MQ,
    /**
     * mqtt
     */
    MQTT

}
