package cn.mesmile.admin.common.rabbit.config;

import cn.mesmile.admin.common.rabbit.constant.RabbitConstant;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

/**
 * @author zb
 * @Description
 */
@Slf4j
@Configuration
public class RabbitMqConfiguration {

    /*
        #    # 开启二次确认 生产者到broker的交换机    默认 none [不开启确认]
        #    publisher-confirm-type: simple
        #    # 开启二次确认 交换机到队列的可靠性投递
        #    publisher-returns: true
        #    # 为true,则交换机处理消息到路由失败，则会返回给生产者       为false 交换机到队列不成功，则丢弃消息（默认）
        #    template:
        #      mandatory: true
	 */

    @Bean
    @SuppressWarnings("all")
	public RabbitTemplate rabbitTemplate(CachingConnectionFactory connectionFactory) {
        // 开启二次确认 生产者到broker的交换机    默认 none [不开启确认]
		connectionFactory.setPublisherConfirmType(CachingConnectionFactory.ConfirmType.SIMPLE);
		// 开启二次确认 交换机到队列的可靠性投递
		connectionFactory.setPublisherReturns(true);
		RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
		// 为true,则交换机处理消息到路由失败，则会返回给生产者   为false 交换机到队列不成功，则丢弃消息（默认）
		rabbitTemplate.setMandatory(true);

		rabbitTemplate.setConfirmCallback((correlationData, ack, cause) ->
                log.info("消息发送成功:correlationData({}),ack({}),cause({})", correlationData, ack, cause)
        );
		rabbitTemplate.setReturnsCallback((ReturnedMessage returned) ->
               log.info("消息丢失:exchange({}),route({}),replyCode({}),replyText({}),message:{}", returned.getExchange(),
                       returned.getRoutingKey(), returned.getReplyCode(), returned.getReplyText(), returned.getMessage())
        );
		return rabbitTemplate;
	}

    /*
      fanout广播模式, 一个交换机可以绑定多个队列，交换机收到数据后会转发消息给多个绑定好的队列
      此模式(fanout)消费者集群情况下，同一个队列的消费者[轮巡]收到消息

      // 队列与交换机绑定
      return BindingBuilder.bind(fanoutOneQueue()).to(fanoutExchange());

                                                ------》队列1 ---> 消费者业务①
      producer  -->  exchange（fanout mode） -》
                                                ------》队列2 ---> 消费者业务②

      发送消息示例：fanout模式下，routingKey可以不用填写
       rabbitTemplate.convertAndSend("交换机","","direct message");
     */
    //////////////////////////////////////fanout mode start///////////////////////////////////////////
    /**
     * fanout模式队列
     */
    @Bean
    public Queue fanoutOneQueue() {
        //
        return new Queue(RabbitConstant.FANOUT_MODE_QUEUE_ONE,true, false, false);
    }
    /**
     * fanout模式队列
     */
    @Bean
    public Queue fanoutTwoQueue() {
        return new Queue(RabbitConstant.FANOUT_MODE_QUEUE_TWO);
    }
    /**
     * fanout模式交换机
     */
    @Bean
    public FanoutExchange fanoutExchange() {
        return new FanoutExchange(RabbitConstant.FANOUT_MODE_EXCHANGE_ONE);
    }
    /**
     * fanout模式 绑定队列与交换机
     */
    @Bean
    public Binding fanoutOneBinding() {
        // 队列与交换机绑定
        return BindingBuilder.bind(fanoutOneQueue()).to(fanoutExchange());
    }
    /**
     * fanout模式 绑定队列与交换机
     */
    @Bean
    public Binding fanoutTwoBinding() {
        // 队列与交换机绑定
        return BindingBuilder.bind(fanoutTwoQueue()).to(fanoutExchange());
    }
    //////////////////////////////////////////////fanout mode end////////////////////////////////////////////


     /*
      direct模式是针对广播模式的一种升级，对绑定的队列进行分类，投递消息时指定一个RoutingKey，
      只有RoutingKey与BindingKey 匹配的队列，消息才会被投递进去
      此模式(direct)消费者集群情况下，同一个队列的消费者[轮巡]收到消息

      根据routingKey/BindingKey将消息投递到队列中去 BindingBuilder.bind(directOneQueue).to(directExchange).with("testRoutingKey");

                                                RoutingKey/BindingKey ------》队列1 ---> 消费者业务①
      producer  -->  exchange（direct mode） -》
                                                RoutingKey/BindingKey ------》队列2 ---> 消费者业务②

      发送消息示例：
       rabbitTemplate.convertAndSend("交换机","routingKey","direct message");
     */
    //////////////////////////////////////direct mode start///////////////////////////////////////////
    /**
     * 直接模式队列
     */
    @Bean
    public Queue directOneQueue() {
        return new Queue(RabbitConstant.DIRECT_MODE_QUEUE_ONE);
    }
    /**
     * 直接模式交换机
     */
    @Bean
    public DirectExchange directExchange() {
        return new DirectExchange(RabbitConstant.DIRECT_MODE_EXCHANGE_ONE);
    }
    /**
     * 直接模式 绑定队列与交换机
     */
    @Bean
    public Binding directBinding() {
        // 队列与交换机绑定,指定交换机与队列绑定的 RoutingKey/BindingKey [默认使用队列名称]
        return BindingBuilder.bind(directOneQueue()).to(directExchange()).withQueueName();
    }
    //////////////////////////////////////////////direct mode end////////////////////////////////////////////


     /*
       topic模式是针对direct模式的一种升级，对绑定的队列进行分类(并且可以通过 routingKey 模糊匹配)，投递消息时指定一个RoutingKey，
      只有RoutingKey与BindingKey匹配的队列，消息才会被投递进去
      此模式(topic)消费者集群情况下，同一个队列的消费者[轮巡]收到消息

      // 队列与交换机绑定
      return BindingBuilder.bind(topicOneQueue()).to(topicOneExchange()).with(RabbitConstant.TOPIC_MODE_ROUTING_KEY);

                                                RoutingKey/BindingKey ------》队列1 ---> 消费者业务①
      producer  -->  exchange（direct mode） -》
                                                RoutingKey/BindingKey ------》队列2 ---> 消费者业务②

      发送消息示例：fanout模式下, routingKey可通过模糊匹配的方式匹配消息
       rabbitTemplate.convertAndSend("交换机","routingKey","direct message");

      主题模式 RoutingKey 说明
        <li>路由格式必须以 . 分隔，比如 user.email 或者 user.aaa.email</li>
        <li>通配符 * ，代表一个占位符，或者说一个单词，比如路由为 user.*，那么 user.email 可以匹配，但是 user.aaa.email 就匹配不了</li>
        <li>通配符 # ，代表一个或多个占位符，或者说一个或多个单词，比如路由为 user.#，那么 user.email 可以匹配，user.aaa.email 也可以匹配</li>

     */
    /////////////////////////////////////topic mode start///////////////////////////////////////////
    /**
     *  topic模式队列
     */
    @Bean
    public Queue topicOneQueue() {
        return new Queue(RabbitConstant.TOPIC_MODE_QUEUE_ONE);
    }
    /**
     * topic模式交换机
     */
    @Bean
    public TopicExchange topicOneExchange() {
        return new TopicExchange(RabbitConstant.TOPIC_MODE_EXCHANGE_ONE);
    }
    /**
     * topic模式 绑定队列与交换机
     *
     *  主题模式 RoutingKey 说明
     *   <li>路由格式必须以 . 分隔，比如 user.email 或者 user.aaa.email</li>
     *   <li>通配符 * ，代表一个占位符，或者说一个单词，比如路由为 user.*，那么 user.email 可以匹配，但是 user.aaa.email 就匹配不了</li>
     *   <li>通配符 # ，代表一个或多个占位符，或者说一个或多个单词，比如路由为 user.#，那么 user.email 可以匹配，user.aaa.email 也可以匹配</li>
     *
     */
    @Bean
    public Binding topicOneBinding() {
        // 队列与交换机绑定
        return BindingBuilder.bind(topicOneQueue()).to(topicOneExchange()).with(RabbitConstant.TOPIC_MODE_ROUTING_KEY);
    }
    //////////////////////////////////////////////topic mode end////////////////////////////////////////////



    /*
       deadLetter模式死信队列

      producer  -->  exchange1（topic mode） -》RoutingKey/BindingKey ----》队列1 【队列1无消费者】，队列1 绑定死信交换机2以及死信路由key

      producer  -->  exchange2（topic mode） -》RoutingKey/BindingKey ------》队列2 ---> 消费者业务②

     */
    /////////////////////////////////////deadLetter mode start///////////////////////////////////////////
    /**
     *  topic模式队列
     */
    @Bean
    public Queue topicTwoQueue() {
        return QueueBuilder.durable(RabbitConstant.TOPIC_MODE_QUEUE_TWO)
                // 设置死信队列绑定的交换机
                .deadLetterExchange(RabbitConstant.DEAD_LETTER_MODE_EXCHANGE_ONE)
                // 设置死信路由key，即 发送到 死信队列 的路由key
                .deadLetterRoutingKey("dead.mode.topic1")
                // 队列消息的过期时间，单位 ms
                .ttl(10000)
                // 声明该队列最多能存放的消息个数
                .maxLength(600).build();
    }
    /**
     * topic模式交换机
     */
    @Bean
    public TopicExchange topicTwoExchange() {
        return new TopicExchange(RabbitConstant.TOPIC_MODE_EXCHANGE_TWO);
    }
    /**
     * topic模式 绑定队列与交换机
     */
    @Bean
    public Binding topicTwoBinding() {
        // 队列与交换机绑定
        return BindingBuilder.bind(topicTwoQueue()).to(topicTwoExchange()).with(RabbitConstant.TOPIC_MODE_ROUTING_KEY_TWO);
    }
    /**
     *  deadLetter模式队列, 用于接收 RabbitConstant.TOPIC_MODE_QUEUE_TWO 未消费的消息
     */
    @Bean
    public Queue deadLetterOneQueue() {
        return new Queue(RabbitConstant.DEAD_LETTER_MODE_QUEUE_ONE);
    }
    /**
     * deadLetter模式交换机
     */
    @Bean
    public TopicExchange deadLetterOneExchange() {
        return new TopicExchange(RabbitConstant.DEAD_LETTER_MODE_EXCHANGE_ONE);
    }
    /**
     * deadLetter模式 绑定队列与交换机
     */
    @Bean
    public Binding deadLetterOneBinding() {
        // 队列与交换机绑定
        return BindingBuilder.bind(deadLetterOneQueue()).to(deadLetterOneExchange())
                .with(RabbitConstant.DEAD_LETTER_MODE_ROUTING_KEY_TWO);
    }
    //////////////////////////////////////////////deadLetter mode end////////////////////////////////////////////


    /**
     * 延迟消息，需要安装插件
     * https://www.rabbitmq.com/community-plugins.html
     *
     * #下载插件 rabbitmq_delayed_message_exchange-3.9.0.ez
     * https://github.com/rabbitmq/rabbitmq-delayed-message-exchange/releases
     * # 拷贝插件
     * docker cp ./rabbitmq_delayed_message_exchange-3.9.0.ez  rabbitmq:/opt/rabbitmq/plugins/
     * # 进入容器内
     * docker exec -it rabbitmq /bin/bash
     * # 查看插件列表
     * rabbitmq-plugins list
     * # 开启插件支持
     * rabbitmq-plugins enable rabbitmq_delayed_message_exchange
     * # 退出容器
     * exit
     * # 重启容器
     * docker restart rabbitmq
     */
    /////////////////////////////////////delay mode start///////////////////////////////////////////

    /**
     * 延迟队列
     */
    @Bean
    public Queue delayQueue() {
        return new Queue(RabbitConstant.DELAY_MODE_QUEUE, true);
    }

    /**
     * 延迟队列交换器, x-delayed-type 和 x-delayed-message 固定
     */
    @Bean
    public CustomExchange delayExchange() {
        Map<String, Object> args = Maps.newHashMap();
        args.put("x-delayed-type", "direct");
        return new CustomExchange(RabbitConstant.DELAY_MODE_EXCHANGE, "x-delayed-message", true, false, args);
    }

    /**
     * 延迟队列绑定自定义交换器
     */
    @Bean
    public Binding delayBinding() {
        return BindingBuilder.bind(delayQueue()).to(delayExchange()).with(RabbitConstant.DELAY_MODE_QUEUE).noargs();
    }
    /////////////////////////////////////delay mode end///////////////////////////////////////////
}
