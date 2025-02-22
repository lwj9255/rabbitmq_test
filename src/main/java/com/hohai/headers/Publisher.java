package com.hohai.headers;

import com.hohai.util.RabbitMQConnectionUtil;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class Publisher {
    public static final String HEADER_EXCHANGE = "header-exchange";
    public static final String HEADER_QUEUE = "header-queue";

    @Test
    public void publish() throws Exception {
        //1. 获取连接对象
        Connection connection = RabbitMQConnectionUtil.getConnection();

        //2. 构建Channel
        Channel channel = connection.createChannel();

        //3. 构建exchange
        channel.exchangeDeclare(HEADER_EXCHANGE, BuiltinExchangeType.HEADERS);

        //4. 构建queue
        channel.queueDeclare(HEADER_QUEUE, true, false, false, null);

        //5.绑定
        Map<String,Object> args = new HashMap<>();
        args.put("x-match", "any");// all
        args.put("name", "jack");
        args.put("age",23);
        channel.queueBind(HEADER_QUEUE, HEADER_EXCHANGE, "",args);

        //6. 发送消息
        String msg = "testheader!";
        Map<String,Object> headers = new HashMap<>();
        headers.put("name", "jack");
//        headers.put("age", 23);
        AMQP.BasicProperties props = new AMQP.BasicProperties()
                .builder()
                .headers(headers)
                .build();

        channel.basicPublish(HEADER_EXCHANGE, "", props, msg.getBytes());

        System.out.println("发送消息成功，header = "+headers);
    }
}
