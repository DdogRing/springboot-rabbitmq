package com.ddogring.rabbitmq.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * @ClassName MyCallback
 * @Author DdogRing
 * @Date 2021/6/26 0026 17:43
 * @Description
 * @Version 1.0
 */
@Slf4j
@Component
public class MyCallback implements RabbitTemplate.ConfirmCallback, RabbitTemplate.ReturnCallback {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @PostConstruct
    public void init() {
        rabbitTemplate.setConfirmCallback(this);
        rabbitTemplate.setReturnCallback(this);
    }

    /**
     * 交换机确认回调方法
     * 1.发消息 交换机接收到了 回调
     *  1.1 correlationData 保存回调消息的id及相关信息
     *  1.2 交换机收到消息 ack=true
     *  1.3 cause null
     * 2.发消息 交换机接收失败了 回调
     *  2.1 correlationData 保存回调消息的id及相关信息
     *  2.2 交换机收到消息 ack=false
     *  2.3 cause 失败的原因
     *
     * @author DdogRing
     * @date 2021/6/26 0026 17:43
     * @param correlationData
     * @param ack
     * @param cause
     * @return void
     */
    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        String id = correlationData != null ? correlationData.getId() : "";
        if (ack) {
            log.info("交换机已经收到了ID:{}的消息", id);
        } else {
            log.info("交换机还未收到ID:{}的消息，由于原因: {}", id, cause);
        }
    }

    /**
     * 可以在当消息传递过程中不可达目的地时将消息返回给生产者
     * 只有不可达目的地的时候 才进行回退
     * @author DdogRing
     * @date 2021/6/27 0027 9:51
     * @param message
     * @param replyCode
     * @param replyText
     * @param exchange
     * @param routingKey
     * @return void
     */
    @Override
    public void returnedMessage(Message message, int replyCode, String replyText, String exchange, String routingKey) {
        log.error("消息: {}, 被交换机 {}退回, 退回原因: {}, 路由key: {}",
                new String(message.getBody()), exchange, replyText, routingKey);
    }
}
