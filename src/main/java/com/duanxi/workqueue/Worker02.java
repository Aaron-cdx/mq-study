package com.duanxi.workqueue;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

/**
 * @author caoduanxi
 * @Date 2021/1/6 11:33
 * @Motto Keep thinking, keep coding!
 * 工作队列，工作者
 */
public class Worker02 {
    // 定义消息队列名称
    private final static String TASK_QUEUE_NAME = "task_queue";

    public static void main(String[] args) throws IOException, TimeoutException {
        // 连接工厂
        ConnectionFactory factory = new ConnectionFactory();
        // 设置连接工厂相关信息
        factory.setHost("localhost");
        factory.setPort(5672);

        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        channel.queueDeclare(TASK_QUEUE_NAME,true,false,false,null);
        System.out.println("[*] Waiting for messages. To exit press CTRL+C");
        // 表示一次处理一条请求，如果是0则表示无限制
        channel.basicQos(1);

        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(),StandardCharsets.UTF_8);
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(" [x] Received '" + message + "'");
            channel.basicAck(delivery.getEnvelope().getDeliveryTag(),false);
//            try{
//                doWork(message);
//            }finally {
//                System.out.println("[x] done");
//                channel.basicAck(delivery.getEnvelope().getDeliveryTag(),false);
//            }
        };
        channel.basicConsume(TASK_QUEUE_NAME,false,deliverCallback,consumerTag -> {});
    }

    private static void doWork(String task){
        for (char c : task.toCharArray()) {
            if (c == '.'){
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
