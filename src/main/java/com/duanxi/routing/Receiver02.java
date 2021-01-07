package com.duanxi.routing;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

/**
 * @author caoduanxi
 * @Date 2021/1/6 11:33
 * @Motto Keep thinking, keep coding!
 * 路由模式接收消息，需要指定route key来绑定具体路由从而接收到自己订阅的路由键的消息
 */
public class Receiver02 {
    // 定义消息队列名称
    private final static String EXCHANGE_NAME = "exchange_direct";

    public static void main(String[] args) throws IOException, TimeoutException {
        // 连接工厂
        ConnectionFactory factory = new ConnectionFactory();
        // 设置连接工厂相关信息
        factory.setHost("localhost");
        factory.setPort(5672);

        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT);

        String queueName = channel.queueDeclare().getQueue();
        // 需要队列需要绑定交换机
        channel.queueBind(queueName, EXCHANGE_NAME, "error");
        channel.queueBind(queueName, EXCHANGE_NAME, "info");
        channel.queueBind(queueName, EXCHANGE_NAME, "warn");
        channel.queueBind(queueName, EXCHANGE_NAME, "debug");

        System.out.println("[*] Waiting for messages. To exit press CTRL+C");
        // 表示一次处理一条请求，如果是0则表示无限制
//        channel.basicQos(1);

        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
            System.out.println(" [x] Received '" + message + "'");
            // 手动确认，可以关闭下方的autoAck=false
//            channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
        };
        channel.basicConsume(queueName, true, deliverCallback, consumerTag -> {
        });
    }

    private static void doWork(String task) {
        for (char c : task.toCharArray()) {
            if (c == '.') {
                try {
                    System.out.println("sleep...");
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }
}
