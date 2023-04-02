package cn.xtong.example.exchange.fanout;

import cn.xtong.example.util.RabbitUtil;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
/**
 * FANOUT（扇出）类型交换机消费者
 * 生产者将消息发送给FANOUT模式交换机，交换机会将消息发送给所有的队列。
 *
 * @author 张晓童
 * @date 2023/4/2 14:23
 */
public class Consumer11 {
    public static final String EXCHANGE_NAME = "fanout_exchange";

    public static void main(String[] args) throws Exception{
        Channel channel = RabbitUtil.createChannel();

        // 定义一个扇出类型的交换机
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.FANOUT);

        // 定义临时队列，当消费者断开连接后，自动删除队列。
        String queueName = channel.queueDeclare().getQueue();

        System.out.println("Consumer11等待接收消息....");

        /**
         * 绑定交换机与队列
         * 1. 队列名称
         * 2. 交换机名称
         * 3. RoutingKey 路由
         */
        channel.queueBind(queueName,EXCHANGE_NAME,"");

        channel.basicConsume(queueName,true, (consumerTag, message) -> {
            System.out.println("Consumer11消费消息："+new String(message.getBody()));
        }, consumerTag -> {});
    }
}
