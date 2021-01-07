package com.duanxi.send.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * @author caoduanxi
 * @Date 2021/1/7 15:25
 * @Motto Keep thinking, keep coding!
 */
@Component
@PropertySource(value = {"classpath:value.properties"})
@ConfigurationProperties(prefix = "rabbitmq")
public class TestGetVale {
    @Value("${rabbitmq.queue}")
    String queue;
    @Value("${rabbitmq.exchange}")
    String exchange;
    @Value("${rabbitmq.routing}")
    String routing;

    public void getValue() {
        System.out.println("*************************************");
        System.out.println("queue:" + queue);
        System.out.println("exchange:" + exchange);
        System.out.println("routing:" + routing);
        System.out.println("*************************************");
    }
}
