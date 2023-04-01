package cn.xtong.example.exchange.topic;

import cn.xtong.example.util.RabbitUtil;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;

/**
 * 消费者
 */
public class Consumer15 {
    public static final String EXCHANGE_NAME = "topic_exchange";

    public static void main(String[] args) throws Exception{
        Channel channel = RabbitUtil.createChannel();

        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.TOPIC);

        String queueName = channel.queueDeclare().getQueue();

        channel.queueBind(queueName,EXCHANGE_NAME,"*.*.rabbit");
        channel.queueBind(queueName,EXCHANGE_NAME,"lazy.#");

        System.out.println("Consumer15等待接收消息....");

        channel.basicConsume(queueName,true, (consumerTag, message) -> {
            System.out.println("接受队列："+queueName+"，绑定键："+message.getEnvelope().getRoutingKey());
        }, consumerTag -> {});
    }
}
