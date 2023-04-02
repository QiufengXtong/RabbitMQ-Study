package cn.xtong.example.message_answer.manual_answer;

import cn.hutool.core.thread.ThreadUtil;
import cn.xtong.example.util.RabbitUtil;
import com.rabbitmq.client.Channel;

/**
 * 手动应答消费者
 *
 * @author 张晓童
 * @date 2023/4/2 14:23
 */
public class Consumer03 {
    public static final String QUEUE_NAME = "manual_answer_queue";

    public static void main(String[] args) throws Exception {
        Channel channel = RabbitUtil.createChannel();

        System.out.println("Consumer03等待接收消息....");

        // 设置手动应答
        boolean authAck = false;
        channel.basicConsume(QUEUE_NAME, authAck, (consumerTag, message) -> {
            ThreadUtil.sleep(3 * 1000);
            System.out.println("消费消息：" + new String(message.getBody()));

            /**
             * 消息手动应答
             * 1.消费标记Tag
             * 2.是否批量应答：true应答所有消息包括传递过来的消息，false代表只应答接受到的那个传递的消息
             */
            channel.basicAck(message.getEnvelope().getDeliveryTag(), false);
        }, (consumerTag) -> System.out.println("消息消费被中断"));
    }
}
