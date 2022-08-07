package cn.mesmile.admin.common.rabbit.handler;

import cn.mesmile.admin.common.rabbit.constant.RabbitConstant;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * @author zb
 * @Description
 */
@Slf4j
@RabbitListener(queues = {RabbitConstant.DEAD_LETTER_MODE_QUEUE_ONE})
@Component
public class DeadLetterHandlerDemo {


    @RabbitHandler
    public void directHandlerManualAck(String messageStruct, Message message, Channel channel) {
        //  如果手动ACK,消息会被监听消费,但是消息在队列中依旧存在,如果 未配置 acknowledge-mode 默认是会在消费完毕后自动ACK掉
        final long deliveryTag = message.getMessageProperties().getDeliveryTag();
        log.info("deadLetter队列，手动ACK，接收消息：{}", messageStruct);
        // 这里抛出异常才会触发重试机制
//        if (true){
//            throw new RuntimeException("测试异常");
//        }
        try {
            // 每次消费一条，消费完成后才进行下一条消费
            channel.basicQos(1);
            // 通知 MQ 消息已被成功消费,可以ACK了
            channel.basicAck(deliveryTag, false);
        } catch (Exception e) {
            try {
                /* 告诉，broker，消息拒绝确认   单条确认   重新入队（会一直重试发送到消费者）
                 channel.basicNack(deliveryTag,false,true);
                 basicNack和basicReject介绍
                 basicReject一次只能拒绝接收一个消息，可以设置是否requeue。
                 basicNack方法可以支持一次0个或多个消息的拒收，可以设置是否requeue。
                 */
                // 处理失败,重新压入MQ，（会一直重试发送到消费者）
                channel.basicRecover();
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
    }
}
