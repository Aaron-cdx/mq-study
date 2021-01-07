package com.duanxi.workqueue;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.MessageProperties;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

/**
 * @author caoduanxi
 * @Date 2021/1/6 11:30
 * @Motto Keep thinking, keep coding!
 * 工作队列，利用休眠来替代当前队列正在工作中
 */
public class NewTask {
    // 定义消息队列名称
    private final static String TASK_QUEUE_NAME = "task_queue";

    public static void main(String[] args) throws IOException, TimeoutException {
        // 连接工厂
        ConnectionFactory factory = new ConnectionFactory();
        // 设置连接工厂相关信息
        factory.setHost("localhost");
        factory.setPort(5672);
        // 构建连接，创建信道 try-with-resource
        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel()) {
            channel.queueDeclare(TASK_QUEUE_NAME, true, false, false, null);
//            String message = String.join(" ",args);
            for (int i = 0; i < 20; i++) {
                String message = "message " + i;
                channel.basicPublish("", TASK_QUEUE_NAME, MessageProperties.PERSISTENT_TEXT_PLAIN, message.getBytes(StandardCharsets.UTF_8));
                System.out.println("[x] Sent '" + message + "'");
            }
        }
    }
}
