package com.ddogring.rabbitmq.listener;

import com.ddogring.rabbitmq.config.DelayedQueueConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @ClassName DelayQueueConsumer
 * @Author DdogRing
 * @Date 2021/6/26 0026 13:40
 * @Description 消费者 基于插件的延时消息
 * @Version 1.0
 */
@Slf4j
@Component
public class DelayQueueConsumer {

    /**
     * 监听消息
     * @author DdogRing
     * @date 2021/6/26 0026 14:00
     * @param message
     * @return void
     */
    @RabbitListener(queues = {DelayedQueueConfig.DELAYED_QUEUE_NAME})
    public void receiveDelayQueue(Message message) {
        String msg = new String(message.getBody());
        log.info("当前时间: {}, 收到延迟队列的消息: {}",
                new Date().toString(), msg);
    }
}
