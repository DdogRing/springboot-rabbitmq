package com.ddogring.rabbitmq.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName DelayedQueueConfig
 * @Author DdogRing
 * @Date 2021/6/26 0026 13:10
 * @Description
 * @Version 1.0
 */
@Configuration
public class DelayedQueueConfig {

    /**
     * 交换机
     */
    public static final String DELAYED_EXCHANGE_NAME = "delayed.exchange";

    /**
     * 队列
     */
    public static final String DELAYED_QUEUE_NAME = "delayed.queue";

    /**
     * routingKey
     */
    public static final String DELAYED_ROUTING_KEY = "delayed.routingkey";

    /**
     * 声明自定义交换机 基于插件
     * @author DdogRing
     * @date 2021/6/26 0026 13:18
     * @param
     * @return org.springframework.amqp.core.CustomExchange
     */
    @Bean(name = "delayedExchange")
    public CustomExchange delayedExchange() {
        Map<String, Object> arguments = new HashMap<>(1);
        // 交换机连接类型
        arguments.put("x-delayed-type", "direct");
        /**
         * 1.交换机名称
         * 2.交换机类型
         * 3.是否持久化
         * 4.是否自动删除
         * 5.其他参数
         */
        return new CustomExchange(DELAYED_EXCHANGE_NAME,
                "x-delayed-message",
                false,
                false,
                arguments);
    }

    /**
     * 声明队列
     * @author DdogRing
     * @date 2021/6/26 0026 13:23
     * @param
     * @return org.springframework.amqp.core.Queue
     */
    @Bean(name = "delayedQueue")
    public Queue delayedQueue() {
        return QueueBuilder
                .durable(DELAYED_QUEUE_NAME)
                .build();
    }

    /**
     * 绑定
     * @author DdogRing
     * @date 2021/6/26 0026 13:29
     * @param delayedExchange
     * @param delayedQueue
     * @return org.springframework.amqp.core.Binding
     */
    @Bean
    public Binding dQBindingDE(@Qualifier("delayedExchange") CustomExchange delayedExchange,
                               @Qualifier("delayedQueue") Queue delayedQueue) {
        return BindingBuilder.bind(delayedQueue)
                .to(delayedExchange)
                .with(DELAYED_ROUTING_KEY)
                .noargs();
    }
}
