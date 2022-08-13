package cn.mesmile.admin.modules.message.operational;

import cn.mesmile.admin.common.rabbit.constant.RabbitConstant;
import cn.mesmile.admin.modules.message.vo.MqMessageVO;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Set;

/**
 * rabbitmq方式实现接口
 * 生产者-直连交换机模式
 * 此模式下路由键匹配，同一账号多处在线都可接收到消息，未登录账号无法接收推送,且再登录账号不会有推送，路由消息丢失，适用于web端
 *
 * @author zb
 * @Description
 */
@Slf4j
@Component
@ConditionalOnProperty(
	name = "admin.message.service-type",
	havingValue = "rabbit_mq"
)
public class RabbitMqSendServiceImpl implements ISendService{

	/**
     rabbitmq-plugins enable rabbitmq_web_stomp rabbitmq_web_stomp_examples
     直连交换机
	String MESSAGE_EXCHANGE_DIRECT = "message.direct.exchange";
	 普通通知消息
	 String MESSAGE_QUEUE_SINGLE_ROUTE_KEY = "message.single_";
	 群发通知消息
	String MESSAGE_QUEUE_GROUP_ROUTE_KEY = "message.group_";
	 */

    @Resource
    public RabbitTemplate rabbitTemplate;

	@Override
	public void sendSingleMessage(String receiveUserId, MqMessageVO mqMessageVO) {
		try {
			rabbitTemplate.convertAndSend(RabbitConstant.MESSAGE_EXCHANGE_DIRECT,
					RabbitConstant.MESSAGE_QUEUE_SINGLE_ROUTE_KEY + receiveUserId, JSONObject.toJSONString(mqMessageVO));
		}catch (Exception e){
			log.error("发送消息失败：",e);
		}
	}

	@Override
	public void sendGroupMessage(Set<String> userSet, MqMessageVO mqMessageVO) {
		try {
			userSet.forEach(
					userId-> rabbitTemplate.convertAndSend(RabbitConstant.MESSAGE_EXCHANGE_DIRECT,
					RabbitConstant.MESSAGE_QUEUE_SINGLE_ROUTE_KEY +userId, JSONObject.toJSONString(mqMessageVO))
			);
		}catch (Exception e){
			log.error("发送消息失败：",e);
		}
	}
}