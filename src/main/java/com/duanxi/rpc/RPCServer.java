package com.duanxi.rpc;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

/**
 * @author caoduanxi
 * @Date 2021/1/6 11:30
 * @Motto Keep thinking, keep coding!
 * RPC模式，RPC remote procedure call远程处理调用，即远程调用某些函数，然后获取结果
 */
public class RPCServer {
    // 定义消息队列名称
    private final static String RPC_QUEUE_NAME = "rpc_queue";

    private static int fin(int n) {
        if (n == 0 || n == 1) return n;
        return fin(n - 1) + fin(n - 2);
    }

    public static void main(String[] args) throws IOException, TimeoutException {
        // 连接工厂
        ConnectionFactory factory = new ConnectionFactory();
        // 设置连接工厂相关信息
        factory.setHost("localhost");
        factory.setPort(5672);

        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel()) {
            channel.queueDeclare(RPC_QUEUE_NAME, false, false, false, null);
            // 这是清除队列中的内容
            channel.queuePurge(RPC_QUEUE_NAME);
            channel.basicQos(1);
            System.out.println(" [x] Awaiting RPC requests");

            Object monitor = new Object();
            // 这是一个回调函数
            DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                // 获取消息中携带的数据
                AMQP.BasicProperties replyProps = new AMQP.BasicProperties
                        .Builder()
                        .correlationId(delivery.getProperties().getCorrelationId())
                        .build();

                String response = "";
                try {
                    String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
                    int n = Integer.parseInt(message);

                    System.out.println(" [.] fib(" + message + ")");
                    response += fin(n);
                } catch (RuntimeException e) {
                    System.out.println(" [.] " + e.toString());
                } finally {
                    // 发布需要反馈给远程调用的结果消息
                    channel.basicPublish("", delivery.getProperties().getReplyTo(), replyProps, response.getBytes(StandardCharsets.UTF_8));
                    // 手动确认
                    channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
                    // 这里用了锁，保证每一时刻只处理一个阶段的请求
                    synchronized (monitor) {
                        monitor.notify();
                    }
                }
            };
            channel.basicConsume(RPC_QUEUE_NAME, false, deliverCallback, consumerTag -> {
            });
            // 等待准备为RPC调用客户端处理请求
            while (true) {
                synchronized (monitor) {
                    try {
                        monitor.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
