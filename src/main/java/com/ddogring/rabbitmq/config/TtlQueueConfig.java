package com.ddogring.rabbitmq.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName TtlQueueConfig
 * @Author DdogRing
 * @Date 2021/6/25 0025 15:57
 * @Description TTL队列 配置文件类代码
 * @Version 1.0
 */
@Configuration
public class TtlQueueConfig {
    /**
     * 普通交换机名称
     */
    public static final String X_EXCHANGE = "X";

    /**
     * 死信交换机名称
     */
    public static final String Y_DEAD_LETTER_EXCHANGE = "Y";

    /**
     * 普通队列QA
     */
    public static final String QUEUE_A = "QA";

    /**
     * 普通队列QB
     */
    public static final String QUEUE_B = "QB";

    /**
     * 死信队列名称QD
     */
    public static final String DEAD_LETTER_QUEUE = "QD";

    /**
     * 通用的队列名称QC
     */
    public static final String COMMON_QUEUE_C = "QC";

    /**
     * 声明xExchange 别名
     * @author DdogRing
     * @date 2021/6/25 0025 16:08
     * @param
     * @return org.springframework.amqp.core.DirectExchange
     */
    @Bean(name = "xExchange")
    public DirectExchange xExchange() {
        return new DirectExchange(X_EXCHANGE);
    }

    /**
     * 声明yExchange
     * @author DdogRing
     * @date 2021/6/25 0025 16:08
     * @param
     * @return org.springframework.amqp.core.DirectExchange
     */
    @Bean(name = "yExchange")
    public DirectExchange yExchange() {
        return new DirectExchange(Y_DEAD_LETTER_EXCHANGE);
    }

    /**
     * 声明队列QA TTL 10秒
     * @author DdogRing
     * @date 2021/6/25 0025 16:13
     * @param
     * @return org.springframework.amqp.core.Queue
     */
    @Bean(name = "queueA")
    public Queue queueA() {
        Map<String, Object> arguments = new HashMap<>(3);
        // 设置死信交换机
        arguments.put("x-dead-letter-exchange", Y_DEAD_LETTER_EXCHANGE);
        // 设置死信RoutingKey
        arguments.put("x-dead-letter-routing-key", "YD");
        // 设置TTL
        arguments.put("x-message-ttl", 10 * 1000);
        return QueueBuilder
                .durable(QUEUE_A)
                .withArguments(arguments)
                .build();
    }

    /**
     * 声明队列QB TTL 40秒
     * @author DdogRing
     * @date 2021/6/25 0025 16:13
     * @param
     * @return org.springframework.amqp.core.Queue
     */
    @Bean(name = "queueB")
    public Queue queueB() {
        Map<String, Object> arguments = new HashMap<>(3);
        // 设置死信交换机
        arguments.put("x-dead-letter-exchange", Y_DEAD_LETTER_EXCHANGE);
        // 设置死信RoutingKey
        arguments.put("x-dead-letter-routing-key", "YD");
        // 设置TTL
        arguments.put("x-message-ttl", 40 * 1000);
        return QueueBuilder
                .durable(QUEUE_B)
                .withArguments(arguments)
                .build();
    }

    /**
     * 声明通用队列QC
     * @author DdogRing
     * @date 2021/6/26 0026 10:45
     * @param
     * @return org.springframework.amqp.core.Queue
     */
    @Bean(name = "queueC")
    public Queue queueC() {
        Map<String, Object> arguments = new HashMap<>(2);
        arguments.put("x-dead-letter-exchange", Y_DEAD_LETTER_EXCHANGE);
        arguments.put("x-dead-letter-routing-key", "YD");
        return QueueBuilder
                .durable(COMMON_QUEUE_C)
                .withArguments(arguments)
                .build();
    }

    /**
     * 声明死信队列 queueD
     * @author DdogRin
     * @date 2021/6/25 0025 16:25
     * @param
     * @return org.springframework.amqp.core.Queue
     */
    @Bean(name = "queueD")
    public Queue queueD() {
        return QueueBuilder
                .durable(DEAD_LETTER_QUEUE)
                .build();
    }

    /**
     * 队列A绑定X交换机
     * @author DdogRing
     * @date 2021/6/25 0025 16:33
     * @param queueA
     * @param xExchange
     * @return org.springframework.amqp.core.Binding
     */
    @Bean(name = "queueABindingX")
    public Binding queueABindingX(@Qualifier("queueA") Queue queueA,
                                  @Qualifier("xExchange") DirectExchange xExchange) {
        return BindingBuilder
                .bind(queueA)
                .to(xExchange)
                .with("XA");
    }

    /**
     * 队列B绑定X交换机
     * @author DdogRing
     * @date 2021/6/25 0025 16:37
     * @param queueB
     * @param xExchange
     * @return org.springframework.amqp.core.Binding
     */
    @Bean(name = "queueBBindingX")
    public Binding queueBBindingX(@Qualifier("queueB")Queue queueB,
                                  @Qualifier("xExchange") DirectExchange xExchange) {
        return BindingBuilder
                .bind(queueB)
                .to(xExchange)
                .with("XB");
    }

    /**
     * 通用队列C绑定X交换机
     * @author DdogRing
     * @date 2021/6/26 0026 10:49
     * @param queueC
     * @param exchangeX
     * @return org.springframework.amqp.core.Binding
     */
    @Bean(name = "queueCBindingX")
    public Binding queueCBindingX(@Qualifier("queueC") Queue queueC,
                                  @Qualifier("xExchange") DirectExchange exchangeX) {
        return BindingBuilder
                .bind(queueC)
                .to(exchangeX)
                .with("XC");
    }

    /**
     * 死信队列D绑定死信交换机Y
     * @author DdogRing
     * @date 2021/6/25 0025 16:39
     * @param
     * @return org.springframework.amqp.core.Binding
     */
    @Bean(name = "queueDBindingY")
    public Binding queueDBindingY(@Qualifier("queueD") Queue queueD,
                                  @Qualifier("yExchange") DirectExchange yExchange) {
        return BindingBuilder
                .bind(queueD)
                .to(yExchange)
                .with("YD");
    }
}
