package cn.xtong.example.exchange.topic;

import cn.xtong.example.util.RabbitUtil;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;

/**
 * TOPIC（主题）类型交换机消费者
 * 生产者将消息发送给TOPIC模式交换机，交换机会根据RoutingKey的规则发送给指定队列。
 *
 * @author 张晓童
 * @date 2023/4/2 14:23
 */
public class Consumer14 {
    public static final String EXCHANGE_NAME = "topic_exchange";

    public static void main(String[] args) throws Exception{
        Channel channel = RabbitUtil.createChannel();

        // 定义一个主题类型的交换机
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.TOPIC);

        String queueName = channel.queueDeclare().getQueue();

        channel.queueBind(queueName,EXCHANGE_NAME,"*.orange.*");

        System.out.println("Consumer14等待接收消息....");

        channel.basicConsume(queueName,true, (consumerTag, message) -> {
            System.out.println("接受队列："+queueName+"，绑定键："+message.getEnvelope().getRoutingKey());
        }, consumerTag -> {});
    }
}
