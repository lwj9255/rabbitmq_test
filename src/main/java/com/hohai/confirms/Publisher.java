package com.hohai.confirms;

import com.hohai.util.RabbitMQConnectionUtil;
import com.rabbitmq.client.*;
import org.junit.Test;

import java.io.IOException;

public class Publisher {

    @Test
    public void publish() throws Exception {
        //1. 获取连接对象
        Connection connection = RabbitMQConnectionUtil.getConnection();

        //2. 构建Channel
        Channel channel = connection.createChannel();

        //3. 构建队列
        channel.queueDeclare("confirms",true,false,false,null);

        //4. 开启confirms
        channel.confirmSelect();

        //5. 设置confirms的异步回调
        channel.addConfirmListener(new ConfirmListener() {
            @Override
            public void handleAck(long l, boolean b) throws IOException {
                System.out.println("消息成功的发送到exchange！");
            }

            @Override
            public void handleNack(long l, boolean b) throws IOException {
                System.out.println("消息没有发送到exchange，尝试重试，或保存到数据库做其他补偿操作！");
            }
        });

        //6. 设置return回调，确认消息是否路由到了queue
        channel.addReturnListener(new ReturnCallback() {
            @Override
            public void handle(Return aReturn) {
                System.out.println("消息没有路由到指定队列！");
            }
        });

        //7. 设置一下消息
        String message = "Hello World!";

        //8. 设置消息持久化,设置好后要把props放到发送消息的函数basicPublish中
        AMQP.BasicProperties props = new AMQP.BasicProperties()
                .builder()
                        .deliveryMode(2) // 设置为2代表持久化
                                .build();

        //9. 在发送消息时，将basicPublish方法参数中的mandatory设置为true，即可开启Return机制，当消息没有路由到队列中时，就会执行return回调
        channel.basicPublish("","confirms",true,props,message.getBytes());
        System.out.println("消息发送成功！");
        System.in.read();

        System.in.read();
    }
}
