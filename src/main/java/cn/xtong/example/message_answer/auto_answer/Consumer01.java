package cn.xtong.example.message_answer.auto_answer;

import cn.hutool.core.thread.ThreadUtil;
import cn.xtong.example.util.RabbitUtil;
import com.rabbitmq.client.Channel;

/**
 * 自动应答消费者
 *
 * @author 张晓童
 * @date 2023/4/2 14:23
 */
public class Consumer01 {
    public static final String QUEUE_NAME = "auto_answer_queue";

    public static void main(String[] args) throws Exception {
        Channel channel = RabbitUtil.createChannel();

        System.out.println("Consumer01等待接收消息....");

        channel.basicConsume(QUEUE_NAME, true, (consumerTag, message) -> {
            // 模拟需要3秒处理消息
            ThreadUtil.sleep(3 * 1000);
            System.out.println("消费消息：" + new String(message.getBody()));
        }, (consumerTag) -> System.out.println("消息被取消"));
    }
}
