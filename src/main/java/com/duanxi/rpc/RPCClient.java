package com.duanxi.rpc;

import com.rabbitmq.client.*;
import com.rabbitmq.client.impl.AMQBasicProperties;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.UUID;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeoutException;

/**
 * @author caoduanxi
 * @Date 2021/1/6 11:33
 * @Motto Keep thinking, keep coding!
 * 主体模式，需要指定route key来绑定具体路由从而接收到自己订阅的路由键的消息
 */
public class RPCClient implements AutoCloseable {
    // 定义消息队列名称
    private final static String RPC_QUEUE_NAME = "rpc_queue";
    private Connection connection;
    private Channel channel;

    public RPCClient() throws IOException, TimeoutException {
        // 连接工厂
        ConnectionFactory factory = new ConnectionFactory();
        // 设置连接工厂相关信息
        factory.setHost("localhost");
        factory.setPort(5672);
        connection = factory.newConnection();
        channel = connection.createChannel();
    }

    public static void main(String[] args) throws IOException, TimeoutException {
        try (RPCClient fibonacciRpc = new RPCClient()) {
            for (int i = 0; i < 32; i++) {
                String int_str = Integer.toString(i);
                System.out.println(" [x] Requesting fib(" + int_str + ")");
                String response = fibonacciRpc.call(int_str);
                System.out.println(" [.] Got '" + response + "'");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 调用服务
     *
     * @param message 发送的消息
     * @return 服务的结果
     */
    private String call(String message) throws IOException, InterruptedException {
        final String corrId = UUID.randomUUID().toString();

        String replyQueueName = channel.queueDeclare().getQueue();
        AMQP.BasicProperties props = new AMQP.BasicProperties
                .Builder()
                .correlationId(corrId)
                .replyTo(replyQueueName)    // 一定要指定是反馈给谁啊
                .build();
        // 消息发布
        channel.basicPublish("", RPC_QUEUE_NAME, props, message.getBytes(StandardCharsets.UTF_8));

        // 利用阻塞队列接收
        final BlockingQueue<String> response = new ArrayBlockingQueue<>(1);
        String ctag = channel.basicConsume(replyQueueName, true, (consumerTag, delivery) -> {
            if (delivery.getProperties().getCorrelationId().equals(corrId)) {
                response.offer(new String(delivery.getBody(), StandardCharsets.UTF_8));
            }
        }, consumerTag -> {
        });
        String result = response.take();
        channel.basicCancel(ctag);
        return result;
    }

    @Override
    public void close() throws Exception {
        connection.close();
    }
}
