package cn.mesmile.admin.modules.message.operational;

import cn.mesmile.admin.modules.message.vo.MqMessageVO;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import net.dreamlu.iot.mqtt.core.server.MqttServer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Set;

/**
 * @author zb
 * @Description
 */
@Slf4j
@ConditionalOnProperty(
		value = {"admin.message.service-type"},
		havingValue = "mqtt"
)
@Component
public class MqttSendServiceImpl implements  ISendService{

	@Resource
	private MqttServer mqttServer;

	@Override
	public void sendSingleMessage(String receiveUserId, MqMessageVO mqMessageVO) {
		boolean b = mqttServer.publishAll("/userId" + receiveUserId,
				ByteBuffer.wrap(JSONObject.toJSONString(mqMessageVO).getBytes(StandardCharsets.UTF_8)));
	}

	@Override
	public void sendGroupMessage(Set<String> userSet, MqMessageVO mqMessageVO) {
		userSet.forEach(
				userId-> this.sendSingleMessage(userId, mqMessageVO)
		);
	}

}