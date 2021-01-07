package com.duanxi.send;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author caoduanxi
 * @Date 2021/1/7 14:51
 * @Motto Keep thinking, keep coding!
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class ReceiveTest {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Test
    public void testReceive() {
        Message receive = rabbitTemplate.receive();
        byte[] body = receive.getBody();
        System.out.println("接收到信息:" + new String(body));
    }

}
