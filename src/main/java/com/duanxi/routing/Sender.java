package com.duanxi.routing;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

/**
 * @author caoduanxi
 * @Date 2021/1/6 11:30
 * @Motto Keep thinking, keep coding!
 * 路由模式：即对应不同的route_key发送不同的消息
 */
public class Sender {
    // 定义消息队列名称
    private final static String EXCHANGE_NAME = "exchange_direct";

    public static void main(String[] args) throws IOException, TimeoutException {
        // 连接工厂
        ConnectionFactory factory = new ConnectionFactory();
        // 设置连接工厂相关信息
        factory.setHost("localhost");
        factory.setPort(5672);
        // 构建连接，创建信道 try-with-resource
        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel()) {
            channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT);
            String infoMessage = "Info Message";
            String warnMessage = "Warn Message";
            String errorMessage = "Error Message";
            String debugMessage = "Debug Message";
            channel.basicPublish(EXCHANGE_NAME, "info", MessageProperties.PERSISTENT_TEXT_PLAIN, infoMessage.getBytes(StandardCharsets.UTF_8));
            channel.basicPublish(EXCHANGE_NAME, "warn", MessageProperties.PERSISTENT_TEXT_PLAIN, warnMessage.getBytes(StandardCharsets.UTF_8));
            channel.basicPublish(EXCHANGE_NAME, "error", MessageProperties.PERSISTENT_TEXT_PLAIN, errorMessage.getBytes(StandardCharsets.UTF_8));
            channel.basicPublish(EXCHANGE_NAME, "debug", MessageProperties.PERSISTENT_TEXT_PLAIN, debugMessage.getBytes(StandardCharsets.UTF_8));
        }
    }
}
