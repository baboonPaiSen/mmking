package com.mmking.client.config;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;



@SpringBootTest
@RunWith(SpringRunner.class)
public class RabbitMqPropertiesTest {

    @Autowired
    private RabbitMqProperties properties;

    @Test
    public void find (){
        String exchange = properties.getExchange();
    }

}