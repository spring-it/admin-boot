package cn.mesmile.admin.modules.message;

import cn.mesmile.admin.common.utils.SpringUtil;
import io.lettuce.core.ScriptOutputType;
import lombok.extern.slf4j.Slf4j;
import net.dreamlu.iot.mqtt.codec.ByteBufferUtil;
import net.dreamlu.iot.mqtt.core.server.MqttServer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PreDestroy;

/**
 * @author zb
 * @Description
 */
@Slf4j
@Configuration(
        proxyBeanMethods = false
)
@EnableConfigurationProperties({MessageProperties.class})
@ConditionalOnProperty(
        value = {"admin.message.enabled"},
        havingValue = "true"
)
public class MessageConfiguration {

    @ConditionalOnProperty(
            value = {"admin.message.service-type"},
            havingValue = "mqtt"
    )
    @Bean
    public MqttServer get(MessageProperties messageProperties) {
        // 注意：为了能接受更多链接（降低内存），请添加 jvm 参数 -Xss129k
        MqttServer mqttServer = MqttServer.create()
                // 默认：0.0.0.0
                .ip(messageProperties.getMqttServerIp())
                // 默认：1883
                .port(messageProperties.getMqttServerPort())
                .webPort(messageProperties.getMqttServerWebsocketPort())
                // 默认为： 8092（mqtt 默认最大消息大小），为了降低内存可以减小小此参数，如果消息过大 t-io 会尝试解析多次
                .readBufferSize(512)
                // 最大包体长度，如果包体过大需要设置此参数，默认为： 8092
                .maxBytesInMessage(1024 * 100)
                // 自定义认证
                .authHandler((context, uniqueId, clientId, userName, password) -> true)
                // 消息监听
                .messageListener((context, clientId, message) -> {
                    System.out.println("sdfkjsdlfkj" + message);
                    log.info("clientId:{} message:{} payload:{}", clientId, message, ByteBufferUtil.toString(message.getPayload()));
                })
                .httpEnable(true)
                .debug() // 开启 debug 信息日志
                .start();
        return mqttServer;
    }


}
