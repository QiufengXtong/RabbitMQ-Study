package cn.xtong.example.message_answer.message_persistence;

import cn.xtong.example.util.RabbitUtil;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.MessageProperties;

import java.nio.charset.StandardCharsets;

/**
 * 消息持久化生产者
 * 注意：设置消息的持久化时，队列必须设置持久化，否则重启rabbitMQ以后，队列消息都会丢失
 *
 * @author 张晓童
 * @date 2023/4/2 14:23
 */
public class Producer {
    public static final String QUEUE_NAME = "message_persistence_queue";

    public static void main(String[] args) throws Exception {
        Channel channel = RabbitUtil.createChannel();

        channel.queueDeclare(QUEUE_NAME, true, false, false, null);

        String message = "你好";
        channel.basicPublish("", QUEUE_NAME, MessageProperties.PERSISTENT_TEXT_PLAIN, message.getBytes(StandardCharsets.UTF_8));
        System.out.println("成功发送消息：" + message);
    }
}
