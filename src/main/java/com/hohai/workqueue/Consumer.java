package com.hohai.workqueue;

import com.hohai.util.RabbitMQConnectionUtil;
import com.rabbitmq.client.*;
import org.junit.Test;

import java.io.IOException;

public class Consumer {
    @Test
    public void consume1() throws Exception {
        //1. 获取连接对象
        Connection connection = RabbitMQConnectionUtil.getConnection();

        //2. 构建Channel
        Channel channel = connection.createChannel();

        //3. 构建队列（消息只有到了队列中才不会丢，因此两边都要声明）
        channel.queueDeclare(Publisher.QUEUE_NAME,false,false,false,null);

        //3.5 设置消息的流控
        channel.basicQos(1);

        //4. 监听消息
        DefaultConsumer callback = new DefaultConsumer(channel){
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("消费者1获取到消息：" + new String(body,"UTF-8"));
                channel.basicAck(envelope.getDeliveryTag(),false);
            }
        };
        channel.basicConsume(Publisher.QUEUE_NAME,false,callback);
        System.out.println("开始监听队列");

        System.in.read();
    }

    @Test
    public void consume2() throws Exception {
        //1. 获取连接对象
        Connection connection = RabbitMQConnectionUtil.getConnection();

        //2. 构建Channel
        Channel channel = connection.createChannel();

        //3. 构建队列（消息只有到了队列中才不会丢，因此两边都要声明）
        channel.queueDeclare(Publisher.QUEUE_NAME,false,false,false,null);

        //3.5 设置消息的流控
        channel.basicQos(1);

        //4. 监听消息
        DefaultConsumer callback = new DefaultConsumer(channel){
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                System.out.println("消费者2获取到消息：" + new String(body,"UTF-8"));
                channel.basicAck(envelope.getDeliveryTag(),false);
            }
        };
        channel.basicConsume(Publisher.QUEUE_NAME,false,callback);
        System.out.println("开始监听队列");

        System.in.read();
    }
}
