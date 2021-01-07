package com.duanxi.simple;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.Queue;
import java.util.concurrent.TimeoutException;

/**
 * @author caoduanxi
 * @Date 2021/1/6 11:05
 * @Motto Keep thinking, keep coding!
 * 消息队列消息接收者
 */
public class Receiver {
    // 定义消息队列名称
    private final static String QUEUE_NAME = "hello";

    public static void main(String[] args) throws IOException, TimeoutException {
        // 连接工厂
        ConnectionFactory factory = new ConnectionFactory();
        // 设置连接工厂相关信息
        factory.setHost("localhost");
        factory.setPort(5672);
        // 构建连接，创建信道
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        /*
         * @param queue the name of the queue   队列的名称
         * @param durable true if we are declaring a durable queue (the queue will survive a server restart) 消息是否持久存活直到消费服务器重启成功
         * @param exclusive true if we are declaring an exclusive queue (restricted to this connection) 排他队列，即严格允许一个同名的队列
         * @param autoDelete true if we are declaring an autodelete queue (server will delete it when no longer in use) 自动删除
         * @param arguments other properties (construction arguments) for the queue 其余参数
         */
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);
        System.out.println("[*] Waiting for messages. To exit press CTRL+C");

        // 开始接收信息
        DeliverCallback deliverCallback = ((consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), "UTF-8");
            System.out.println("[x] Received '" + message + "'");
        });
        // 基础消费的反馈，队列，自动确认，消费的反馈，取消的反馈
        channel.basicConsume(QUEUE_NAME,true,deliverCallback,consumerTag -> {});
    }
}
