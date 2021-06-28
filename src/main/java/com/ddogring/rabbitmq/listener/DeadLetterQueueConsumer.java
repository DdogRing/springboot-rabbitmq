package com.ddogring.rabbitmq.listener;

import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @ClassName DeadLetterQueueConsumer
 * @Author DdogRing
 * @Date 2021/6/25 0025 17:43
 * @Description 队列TTL 消费者
 * @Version 1.0
 */
@Slf4j
@Component
public class DeadLetterQueueConsumer {
    /**
     * 接收消息
     * @author DdogRing
     * @date 2021/6/26 0026 9:53
     * @param message
     * @param channel
     * @return void
     */
    @RabbitListener(queues = "QD")
    public void receiveD(Message message, Channel channel) throws Exception {
        String msg = new String(message.getBody(), "UTF-8");
        log.info("当期时间: {}, 收到死信消息队列的信息: {}", new Date().toString(), msg);
    }
}
