package com.duanxi.send;

import com.duanxi.send.config.TestGetVale;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author caoduanxi
 * @Date 2021/1/7 14:56
 * @Motto Keep thinking, keep coding!
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class SendTest {
    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Autowired
    private TestGetVale testGetVale;


    @Test
    public void sendMessage() {
        testGetVale.getValue();
        String message = "hello rabbitmq";
        System.out.println("发送消息:" + message);
//        rabbitTemplate.convertAndSend(message);
        for (int i = 0; i < 1000; i++) {
            rabbitTemplate.convertAndSend("amqp_exchange", "test.amqp", message + i);
        }
    }
}
