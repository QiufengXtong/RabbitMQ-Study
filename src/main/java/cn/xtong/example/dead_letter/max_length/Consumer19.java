package cn.xtong.example.dead_letter.max_length;

import cn.xtong.example.util.RabbitUtil;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;

/**
 * 死信队列最大长度消费者
 *
 * @author： 张晓童
 * @date： 2023/4/2 15:16
 */
public class Consumer19 {
    // 私信交换机名称
    public static final String DEAD_EXCHANGE_NAME = "max_length_dead_exchange";
    // 死信队列名称
    public static final String DEAD_QUEUE_NAME = "max_length_dead_queue";

    public static void main(String[] args) throws Exception {
        Channel channel = RabbitUtil.createChannel();

        // 定义死信交换机
        channel.exchangeDeclare(DEAD_EXCHANGE_NAME, BuiltinExchangeType.DIRECT);
        // 定义死信队列
        channel.queueDeclare(DEAD_QUEUE_NAME, false, false, false, null);
        // 死信交换机队列绑定
        channel.queueBind(DEAD_QUEUE_NAME,DEAD_EXCHANGE_NAME,"lisi");

        System.out.println("Consumer19等待接收消息....");

        channel.basicConsume(DEAD_QUEUE_NAME, true, (consumerTag, message) -> {
            System.out.println("Consumer19消费消息："+new String(message.getBody()));
        }, consumerTag -> {
        });


    }
}
