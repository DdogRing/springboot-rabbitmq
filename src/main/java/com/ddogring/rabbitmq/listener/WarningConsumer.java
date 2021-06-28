package com.ddogring.rabbitmq.listener;

import com.ddogring.rabbitmq.config.ConfirmConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * @ClassName WarningConsumer
 * @Author DdogRing
 * @Date 2021/6/27 0027 10:45
 * @Description 报警消费者
 * @Version 1.0
 */
@Slf4j
@Component
public class WarningConsumer {

    @RabbitListener(queues = {ConfirmConfig.WARNING_QUEUE_NAME})
    public void receiveWarningMessage(Message message) {
        log.warn("报警发现不可路由消息: {}", new String(message.getBody()));
    }
}
