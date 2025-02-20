package com.hohai.util;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class RabbitMQConnectionUtil {
    public static final String RABBITMQ_HOST = "121.248.201.14";

    public static final int RABBITMQ_PORT = 5672;

    public static final String RABBITMQ_USERNAME = "guest";

    public static final String RABBITMQ_PASSWORD = "guest";

    public static final String RABBITMQ_VIRTUAL_HOST = "/";

    /**
     * 构建rabbitmq的连接对象
     * @return
     */
    public static Connection getConnection() throws Exception{
        //1. 创建Connection工厂
        ConnectionFactory factory = new ConnectionFactory();

        //2. 设置RabbitMQ的连接信息
        factory.setHost(RABBITMQ_HOST);
        factory.setPort(RABBITMQ_PORT);
        factory.setUsername(RABBITMQ_USERNAME);
        factory.setPassword(RABBITMQ_PASSWORD);
        factory.setVirtualHost(RABBITMQ_VIRTUAL_HOST);

        //3. 返回连接对象
        Connection connection = factory.newConnection();
        return connection;

    }
}
