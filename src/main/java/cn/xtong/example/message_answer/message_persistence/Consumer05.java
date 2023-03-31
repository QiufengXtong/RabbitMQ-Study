package cn.xtong.example.message_answer.message_persistence;

import cn.hutool.core.thread.ThreadUtil;
import cn.xtong.example.util.RabbitUtil;
import com.rabbitmq.client.Channel;

/**
 * 消费者Consumer05
 */
public class Consumer05 {
    public static final String QUEUE_NAME = "message_persistence_queue";

    public static void main(String[] args) throws Exception {
        Channel channel = RabbitUtil.createChannel();

        System.out.println("Consumer05等待接收消息....");

        channel.basicConsume(QUEUE_NAME, true, (consumerTag, message) -> {
            ThreadUtil.sleep(3 * 1000);
            System.out.println("消费消息：" + new String(message.getBody()));
        }, (consumerTag) -> System.out.println("消息消费被中断"));
    }
}
