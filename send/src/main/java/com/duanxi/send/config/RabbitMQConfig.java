package com.duanxi.send.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;


/**
 * @author caoduanxi
 * @Date 2021/1/7 14:47
 * @Motto Keep thinking, keep coding!
 */
@Configuration
public class RabbitMQConfig {

    // 声明队列
    @Bean
    public Queue queue() {
        return new Queue("amqp_queue");
    }

    // 声明交换机
    @Bean
    public TopicExchange topicExchange() {
        return new TopicExchange("amqp_exchange");
    }

    // 绑定队列和交换机
    @Bean
    public Binding binding() {
        return BindingBuilder.bind(queue()).to(topicExchange()).with("*.amqp.#");
    }
}
