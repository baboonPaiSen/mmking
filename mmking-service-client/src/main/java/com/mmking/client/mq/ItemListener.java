package com.mmking.client.mq;

import com.mmking.client.service.CmsClientService;
import com.mmking.utils.JsonUtils;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class ItemListener {
    @Autowired
    private CmsClientService cmsClientService;

    /**
     * 接收传入的消息，下载静态页
     * @param mqMsg
     */

    @RabbitListener(bindings = @QueueBinding(
            value =@Queue(value = "${mmking.mq.queue}",durable = "true"),
            exchange = @Exchange(name = "${mmking.mq.exchange}",type = ExchangeTypes.TOPIC),
            key = {"html.create","html.update","html.delete"}

    ))
    public  void  listenerItem(String  mqMsg, Channel channel, Message message) throws Exception{
        Map<String,String> map = JsonUtils.parse(mqMsg, Map.class);

            cmsClientService.saveHtmlToPath(map.get("pageId"));



    }
}
