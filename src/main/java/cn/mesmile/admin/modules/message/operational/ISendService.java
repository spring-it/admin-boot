package cn.mesmile.admin.modules.message.operational;

import cn.mesmile.admin.modules.message.vo.MqMessageVO;

import java.util.Set;

/**
 * @author zb
 * @Description
 */
public interface ISendService {

    /**
     * 发送普通消息
     * @param receiveUserId 接收人id
     * @param mqMessageVO mq消息实体
     */
    void sendSingleMessage(String receiveUserId, MqMessageVO mqMessageVO);


    /**
     * 发送多条消息
     * @param userSet 多个接收人id
     * @param mqMessageVO mq消息实体
     */
    void sendGroupMessage(Set<String> userSet, MqMessageVO mqMessageVO);

}
