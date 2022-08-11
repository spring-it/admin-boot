package cn.mesmile.admin.common.rabbit.constant;

/**
 * @author zb
 * @Description
 */
public interface RabbitConstant {

    //////////////////////////////////////direct////////////////////////////////////////
    /**
     * direct模式队列
     */
    String DIRECT_MODE_QUEUE_ONE = "direct.mode.queue.one";

    /**
     * direct模式交换机
     */
    String DIRECT_MODE_EXCHANGE_ONE = "direct.mode.exchange.one";

    //////////////////////////////////////fanout////////////////////////////////////////
    /**
     * fanout模式队列
     */
    String FANOUT_MODE_QUEUE_ONE = "fanout.mode.queue.one";

    /**
     * fanout模式队列
     */
    String FANOUT_MODE_QUEUE_TWO = "fanout.mode.queue.two";

    /**
     * fanout模式交换机
     */
    String FANOUT_MODE_EXCHANGE_ONE = "fanout.mode.exchange.one";


    /////////////////////////////////////topic/////////////////////////////////////////////
    /**
     * topic模式队列
     */
    String TOPIC_MODE_QUEUE_ONE = "topic.mode.queue.one";

    /**
     * topic模式交换机
     */
    String TOPIC_MODE_EXCHANGE_ONE = "topic.mode.exchange.one";

    /**
     * topic模式 routingKey
     */
    String TOPIC_MODE_ROUTING_KEY = "topic.mode.#";

    /////////////////////////////////////deadLetter/////////////////////////////////////////////
    /**
     * topic模式队列
     */
    String TOPIC_MODE_QUEUE_TWO = "topic.mode.queue.two";

    /**
     * topic模式交换机
     */
    String TOPIC_MODE_EXCHANGE_TWO = "topic.mode.exchange.two";

    /**
     * topic模式 routingKey
     */
    String TOPIC_MODE_ROUTING_KEY_TWO = "topic.dead.letter.#";

    /**
     * deadLetter模式队列
     */
    String DEAD_LETTER_MODE_QUEUE_ONE= "dead.letter.mode.queue.one";

    /**
     * deadLetter模式交换机
     */
    String DEAD_LETTER_MODE_EXCHANGE_ONE = "dead.letter.mode.exchange.one";

    /**
     * deadLetter模式 routingKey
     */
    String DEAD_LETTER_MODE_ROUTING_KEY_TWO = "dead.mode.#";

    ////////////////////////////////////////delay mode////////////////////////////////////////////////
    /**
     * delay模式队列
     */
    String DELAY_MODE_QUEUE = "delay.mode.queue";
    /**
     * delay交换机
     */
    String DELAY_MODE_EXCHANGE = "delay.mode.exchange";


    //////////////////////////////////////rabbit 消息插件 rabbitmq_web_stomp/////////////////////////
    // rabbitmq-plugins enable rabbitmq_web_stomp rabbitmq_web_stomp_examples
    /**
     * 直连交换机
     */
    String MESSAGE_EXCHANGE_DIRECT = "message.direct.exchange";

    /**
     * 普通通知消息
     */
    String MESSAGE_QUEUE_SINGLE_ROUTE_KEY = "message.single";

    /**
     * 群发通知消息
     */
    String MESSAGE_QUEUE_GROUP_ROUTE_KEY = "message.group";
}
