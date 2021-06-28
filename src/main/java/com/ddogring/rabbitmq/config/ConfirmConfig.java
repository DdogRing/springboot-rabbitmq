package com.ddogring.rabbitmq.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @ClassName ConfirmConfig
 * @Author DdogRing
 * @Date 2021/6/26 0026 16:08
 * @Description 配置类 发布确认 （高级）
 * @Version 1.0
 */
@Configuration
public class ConfirmConfig {

    /**
     * 交换机
     */
    public static final String CONFIRM_EXCHANGE_NAME = "confirm.exchange";

    /**
     * 队列
     */
    public static final String CONFIRM_QUEUE_NAME = "confirm.queue";

    /**
     * routingKey
     */
    public static final String CONFIRM_ROUTING_KEY = "key1";

    /**
     * 备份交换机
     */
    public static final String BACKUP_EXCHANGE_NAME = "backup.exchange";

    /**
     * 备份队列
     */
    public static final String BACKUP_QUEUE_NAME = "backup.queue";

    /**
     * 报警队列
     */
    public static final String WARNING_QUEUE_NAME = "warning.queue";

    @Bean(name = "confirmExchange")
    public DirectExchange confirmExchange() {
        return ExchangeBuilder
                .directExchange(CONFIRM_EXCHANGE_NAME)
                .withArgument("alternate-exchange", BACKUP_EXCHANGE_NAME)
                .build();
    }

    @Bean(name = "backupExchange")
    public FanoutExchange backupExchange() {
        return new FanoutExchange(BACKUP_EXCHANGE_NAME);
    }

    @Bean(name = "confirmQueue")
    public Queue confirmQueue() {
        return new Queue(CONFIRM_QUEUE_NAME);
    }

    @Bean(name = "backupQueue")
    public Queue backupQueue() {
        return QueueBuilder.durable(BACKUP_QUEUE_NAME).build();
    }

    @Bean(name = "warningQueue")
    public Queue warningQueue() {
        return QueueBuilder.durable(WARNING_QUEUE_NAME).build();
    }

    @Bean(name = "cQBindingCE")
    public Binding cQBindingCE(@Qualifier("confirmExchange") DirectExchange confirmExchange,
                               @Qualifier("confirmQueue") Queue queue) {
        return BindingBuilder.bind(queue)
                .to(confirmExchange)
                .with(CONFIRM_ROUTING_KEY);
    }

    @Bean(name = "bQBindingBE")
    public Binding bQBindingBE(@Qualifier("backupExchange") FanoutExchange backupExchange,
                               @Qualifier("backupQueue") Queue backupQueue) {
        return BindingBuilder.bind(backupQueue).to(backupExchange);
    }

    @Bean(name = "wQBindingBE")
    public Binding wQBindingBE(@Qualifier("backupExchange") FanoutExchange backupExchange,
                               @Qualifier("warningQueue") Queue warningQueue) {
        return BindingBuilder.bind(warningQueue).to(backupExchange);
    }
}
