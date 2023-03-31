package cn.xtong.example.hello;

import cn.xtong.example.util.RabbitUtil;
import com.rabbitmq.client.Channel;

import java.nio.charset.StandardCharsets;

/**
 * 生产者
 */
public class Producer {
    public static final String QUEUE_NAME = "hello";

    public static void main(String[] args) throws Exception {
        Channel channel = RabbitUtil.createChannel();

        /**
         * 生成一个队列
         * 1.队列名称
         * 2.是否持久化到硬盘，true持久化，false不持久化
         * 3.消息是否共享，true共享，false不共享
         * 4.最后一个消费者断开连接后是否自动删除，true自动删除，false不自动删除。
         * 5.其他参数
         */
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);
        String message = "hello world";

        /**
         * 发送一个消息
         * 1. 发送到哪个交换机
         * 2. 队列名称
         * 3. 其他参数信息
         * 4. 发送消息的消费体
         */
        channel.basicPublish("", QUEUE_NAME, null, message.getBytes(StandardCharsets.UTF_8));
        System.out.println("消息发送完毕");
    }
}
