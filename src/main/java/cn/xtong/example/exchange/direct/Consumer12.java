package cn.xtong.example.exchange.direct;

import cn.xtong.example.util.RabbitUtil;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;

/**
 * 消费者
 * DIRECT模式：生产者将消息发送给DIRECT模式交换机，交换机会将消息发送给指定RoutingKey的队列。
 */
public class Consumer12 {
    public static final String EXCHANGE_NAME = "direct_exchange";

    public static void main(String[] args) throws Exception{
        Channel channel = RabbitUtil.createChannel();

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
