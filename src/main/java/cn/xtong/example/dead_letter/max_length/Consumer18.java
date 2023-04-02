package cn.xtong.example.dead_letter.max_length;

import cn.xtong.example.util.RabbitUtil;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;

import java.util.HashMap;
import java.util.Map;

/**
 * 死信队列最大长度消费者
 *
 * @author： 张晓童
 * @date： 2023/4/2 15:16
 */
public class Consumer18 {
    // 普通交换机名称
    public static final String NORMAL_EXCHANGE_NAME = "max_length_normal_exchange";
    // 私信交换机名称
    public static final String DEAD_EXCHANGE_NAME = "max_length_dead_exchange";
    // 普通队列名称
    public static final String NORMAL_QUEUE_NAME = "max_length_normal_queue";
    // 死信队列名称
    public static final String DEAD_QUEUE_NAME = "max_length_dead_queue";

    public static void main(String[] args) throws Exception {
        Channel channel = RabbitUtil.createChannel();

        // 定义普通交换机和死信交换机
        channel.exchangeDeclare(NORMAL_EXCHANGE_NAME, BuiltinExchangeType.DIRECT);
        channel.exchangeDeclare(DEAD_EXCHANGE_NAME, BuiltinExchangeType.DIRECT);

        // 普通队列的参数
        Map<String, Object> arguments = new HashMap<>();
        // 死信交换机设置
        arguments.put("x-dead-letter-exchange", DEAD_EXCHANGE_NAME);
        // 死信RoutingKey设置
        arguments.put("x-dead-letter-routing-key", "lisi");
        arguments.put("x-max-length", 5);

        // 定义普通队列和死信队列
        channel.queueDeclare(NORMAL_QUEUE_NAME, false, false, false, arguments);
        channel.queueDeclare(DEAD_QUEUE_NAME, false, false, false, null);


        // 普通交换机队列绑定
        channel.queueBind(NORMAL_QUEUE_NAME,NORMAL_EXCHANGE_NAME,"zhangsan");
        // 死信交换机队列绑定
        channel.queueBind(DEAD_QUEUE_NAME,DEAD_EXCHANGE_NAME,"lisi");

        System.out.println("Consumer18等待接收消息....");


        channel.basicConsume(NORMAL_QUEUE_NAME, true, (consumerTag, message) -> {
            System.out.println("Consumer18消费消息："+new String(message.getBody()));
        }, consumerTag -> {
        });


    }
}
