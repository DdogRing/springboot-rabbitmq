package com.ddogring.rabbitmq.listener;

import com.ddogring.rabbitmq.config.ConfirmConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * @ClassName Consumer
 * @Author DdogRing
 * @Date 2021/6/26 0026 17:31
 * @Description 接收消息
 * @Version 1.0
 */
@Slf4j
@Component
public class Consumer {
    @RabbitListener(queues = {ConfirmConfig.CONFIRM_QUEUE_NAME})
    public void receiveMessage(Message message) {
        log.info("接收到的队列confirm.queue消息: {}", new String(message.getBody()));
    }
}
