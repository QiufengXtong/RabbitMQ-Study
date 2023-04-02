package cn.xtong.example.exchange.direct;

import cn.xtong.example.util.RabbitUtil;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;

/**
 * DIRECT（直接）类型交换机消费者
 * 生产者将消息发送给DIRECT类型交换机，交换机会将消息发送给指定RoutingKey的队列。
 *
 * @author 张晓童
 * @date 2023/4/2 14:23
 */
public class Consumer12 {
    public static final String EXCHANGE_NAME = "direct_exchange";

    public static void main(String[] args) throws Exception{
        Channel channel = RabbitUtil.createChannel();

        // 定义一个直接类型的交换机
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT);

        String queueName = channel.queueDeclare().getQueue();

        channel.queueBind(queueName,EXCHANGE_NAME,"info");
        channel.queueBind(queueName,EXCHANGE_NAME,"warning");

        System.out.println("Consumer12等待接收消息....");

        channel.basicConsume(queueName,true, (consumerTag, message) -> {
            System.out.println("Consumer12消费消息："+new String(message.getBody()));
        }, consumerTag -> {});
    }
}
