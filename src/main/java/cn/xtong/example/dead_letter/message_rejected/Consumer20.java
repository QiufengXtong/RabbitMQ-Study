package cn.xtong.example.dead_letter.message_rejected;

import cn.hutool.core.util.ObjectUtil;
import cn.xtong.example.util.RabbitUtil;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;

import java.util.HashMap;
import java.util.Map;

/**
 * 死信队列消息被拒消费者
 * 注意：需要改成手动应答
 *
 * @author： 张晓童
 * @date： 2023/4/2 15:16
 */
public class Consumer20 {
    // 普通交换机名称
    public static final String NORMAL_EXCHANGE_NAME = "message_rejected_normal_exchange";
    // 私信交换机名称
    public static final String DEAD_EXCHANGE_NAME = "message_rejected_dead_exchange";
    // 普通队列名称
    public static final String NORMAL_QUEUE_NAME = "message_rejected_normal_queue";
    // 死信队列名称
    public static final String DEAD_QUEUE_NAME = "message_rejected_dead_queue";

    public static void main(String[] args) throws Exception {
        Channel channel = RabbitUtil.createChannel();

        // 定义普通交换机和死信交换机
        channel.exchangeDeclare(NORMAL_EXCHANGE_NAME, BuiltinExchangeType.DIRECT);
        channel.exchangeDeclare(DEAD_EXCHANGE_NAME, BuiltinExchangeType.DIRECT);

        // 普通队列的参数
        Map<String, Object> arguments = new HashMap<>();
        arguments.put("x-dead-letter-exchange", DEAD_EXCHANGE_NAME);
        arguments.put("x-dead-letter-routing-key", "lisi");

        // 定义普通队列和死信队列
        channel.queueDeclare(NORMAL_QUEUE_NAME, false, false, false, arguments);
        channel.queueDeclare(DEAD_QUEUE_NAME, false, false, false, null);


        // 普通交换机队列绑定
        channel.queueBind(NORMAL_QUEUE_NAME, NORMAL_EXCHANGE_NAME, "zhangsan");
        // 死信交换机队列绑定
        channel.queueBind(DEAD_QUEUE_NAME, DEAD_EXCHANGE_NAME, "lisi");

        System.out.println("Consumer20等待接收消息....");

        channel.basicConsume(NORMAL_QUEUE_NAME, false, (consumerTag, message) -> {
            String messageInfo = new String(message.getBody());
            if (ObjectUtil.equal(messageInfo, "CC")) {
                channel.basicReject(message.getEnvelope().getDeliveryTag(), false);
                System.out.println("Consumer20拒绝消费消息：" + messageInfo);
            } else {
                System.out.println("Consumer20消费消息：" + messageInfo);
            }

        }, consumerTag -> {
        });


    }
}
