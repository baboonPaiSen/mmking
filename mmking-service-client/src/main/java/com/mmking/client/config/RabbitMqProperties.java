package com.mmking.client.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@ConfigurationProperties(prefix = "mmking.mq")
@Component
public class RabbitMqProperties {

    /*消息队列*/
    public String queue;
    /*通配符*/
    public String routingKey;

    /*交换机*/
    public String exchange;

}
