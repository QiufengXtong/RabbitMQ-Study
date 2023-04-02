package cn.xtong.example.message_answer.unfair_distribute;

import cn.hutool.core.thread.ThreadUtil;
import cn.xtong.example.util.RabbitUtil;
import com.rabbitmq.client.Channel;

/**
 * 不公平分发消费者
 * 注意：不公平分发需要，手动应答，自动应答无效
 *
 * @author 张晓童
 * @date 2023/4/2 14:23
 */
public class Consumer06 {
    public static final String QUEUE_NAME = "unfair_distribute_queue";

    public static void main(String[] args) throws Exception {
        Channel channel = RabbitUtil.createChannel();

        System.out.println("Consumer06等待接收消息....");

        // 设置不公平分发
        channel.basicQos(1);
        channel.basicConsume(QUEUE_NAME, false, (consumerTag, message) -> {
            ThreadUtil.sleep(3 * 1000);
            System.out.println("消费消息：" + new String(message.getBody()));
            channel.basicAck(message.getEnvelope().getDeliveryTag(), false);
        },  (consumerTag) -> System.out.println("消息消费被中断"));
    }
}
