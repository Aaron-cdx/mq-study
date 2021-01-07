package com.duanxi.receive.config;

import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * @author caoduanxi
 * @Date 2021/1/7 14:59
 * @Motto Keep thinking, keep coding!
 * 监听消息
 */
@Component
@RabbitListener(queues = "amqp_queue")
public class RabbitListen {
    @RabbitHandler
    public void receive(String message) {
        System.out.println("接收到消息:" + message);
    }
}
