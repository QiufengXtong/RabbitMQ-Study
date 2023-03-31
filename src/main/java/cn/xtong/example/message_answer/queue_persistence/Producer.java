package cn.xtong.example.message_answer.queue_persistence;

import cn.xtong.example.util.RabbitUtil;
import com.rabbitmq.client.Channel;

/**
 * 生产者
 * 队列持久化：重启RabbitMQ服务队列还会存在。
 */
public class Producer {
    public static final String QUEUE_NAME = "persistence_queue";

    public static void main(String[] args) throws Exception {
        Channel channel = RabbitUtil.createChannel();

        // 队列持久化设置，true为持久化，false为不持久化
        boolean durable = true;
        channel.queueDeclare(QUEUE_NAME, durable, false, false, null);
    }
}
