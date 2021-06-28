package com.ddogring.rabbitmq.controller;

import com.ddogring.rabbitmq.config.DelayedQueueConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

/**
 * @ClassName SendMsgController
 * @Author DdogRing
 * @Date 2021/6/25 0025 17:34
 * @Description 发送延迟消息   http://localhost:8080/ttl/sendMsg/
 * @Version 1.0
 */
@Slf4j
@RestController
@RequestMapping("/ttl")
public class SendMsgController {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * 开始发消息
     * @author DdogRing
     * @date 2021/6/26 0026 10:57
     * @param msg
     * @return
     */
    @GetMapping("/sendMsg/{msg}")
    public void sendMsg(@PathVariable String msg) {
        log.info("当前时间: {}, 发送一条信息给两个TTL队列: {}", new Date().toString(), msg);
        rabbitTemplate.convertAndSend("X", "XA", "消息来自TTL为10s的队列:" + msg);
        rabbitTemplate.convertAndSend("X", "XB", "消息来自TTL为40s的队列:" + msg);
    }

    /**
     * 开始发消息 ttl
     * @author DdogRing
     * @date 2021/6/26 0026 11:03
     * @param message
     * @param ttlTime
     * @return void
     */
    @RequestMapping(value = "/sendExpirationMsg/{message}/{ttlTime}", method = RequestMethod.GET)
    public void sendMsg(@PathVariable(name = "message", value = "message") String message,
                        @PathVariable(name = "ttlTime", value = "ttlTime") String ttlTime) {
        log.info("当前时间: {}, 发送一条时长{}毫秒的TTL消息给队列QC: {}",
                new Date().toString(), ttlTime, message);
        rabbitTemplate.convertAndSend("X","XC", message, msg -> {
            // 发送消息的时候 延迟时长
            msg.getMessageProperties().setExpiration(ttlTime);
            return msg;
        });
    }

    /**
     * 基于延时插件的发送消息
     * @author DdogRing
     * @date 2021/6/26 0026 14:05
     * @param msg
     * @param delayTime
     * @return void
     */
    @GetMapping("/sendDelayMsg/{msg}/{delayTime}")
    public void  sendMsg(@PathVariable(name = "msg", value = "msg") String msg,
                        @PathVariable(name = "delayTime", value = "delayTime") Integer delayTime) {
        log.info("当前时间: {}, 发送一条时长{}毫秒的消息给延迟队列delayed.queue: {}",
                new Date().toString(), delayTime, msg);
        rabbitTemplate.convertAndSend(DelayedQueueConfig.DELAYED_EXCHANGE_NAME,
                DelayedQueueConfig.DELAYED_ROUTING_KEY,
                msg, message -> {
                    // 设置延时消息 延时时间 ms
                    message.getMessageProperties().setDelay(delayTime);
                    return message;
                });
    }
}
