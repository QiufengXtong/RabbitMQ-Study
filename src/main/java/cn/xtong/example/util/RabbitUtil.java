package cn.xtong.example.util;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

/**
 * RabbitMQ 工具类
 */
public class RabbitUtil {

    /**
     * 创建连接工厂
     * @return 连接工厂对象
     */
    private static ConnectionFactory getConnectionFactory(){
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("101.43.185.176");
        connectionFactory.setUsername("root");
        connectionFactory.setPassword("root");
        return connectionFactory;
    }

    /**
     * 创建连接
     * @return 连接对象
     * @throws Exception
     */
    public static Connection createConnection() throws Exception {
        ConnectionFactory connectionFactory = getConnectionFactory();
        return connectionFactory.newConnection();
    }

    /**
     * 创建信道
     * @return 信道对象
     * @throws Exception
     */
    public static Channel createChannel() throws Exception {
        return createChannel(null);
    }

    /**
     * 创建信道
     * @param connection 连接对象
     * @return 信道对象
     * @throws Exception
     */
    public static Channel createChannel(Connection connection) throws Exception {
        if (connection == null) {
            connection = createConnection();
        }
        return connection.createChannel();
    }
}
