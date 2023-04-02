package cn.xtong.example.dead_letter.ttl;

import cn.xtong.example.util.RabbitUtil;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * 死信队列TTL过期生产者
 *
 * @author： 张晓童
 * @date： 2023/4/2 15:31
 */
public class Producer {

    // 普通交换机名称
    public static final String NORMAL_EXCHANGE_NAME = "ttl_normal_exchange";
    // 死信交换机名称
    public static final String DEAD_EXCHANGE_NAME = "ttl_dead_exchange";
    // 普通队列名称
    public static final String NORMAL_QUEUE_NAME = "ttl_normal_queue";

    public static void main(String[] args) throws Exception {
        Channel channel = RabbitUtil.createChannel();

        channel.exchangeDeclare(NORMAL_EXCHANGE_NAME, BuiltinExchangeType.DIRECT);

        // 普通队列的参数
        Map<String, Object> arguments = new HashMap<>();
        // 死信交换机设置
        arguments.put("x-dead-letter-exchange", DEAD_EXCHANGE_NAME);
        // 死信RoutingKey设置
        arguments.put("x-dead-letter-routing-key", "lisi");
        channel.queueDeclare(NORMAL_QUEUE_NAME, false, false, false, arguments);

        AMQP.BasicProperties basicProperties = new AMQP.BasicProperties().builder().expiration("10000").build();

        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            String message = scanner.next();
            channel.basicPublish(NORMAL_EXCHANGE_NAME, "zhangsan", basicProperties, message.getBytes(StandardCharsets.UTF_8));
            System.out.println("成功发送消息：" + message);
        }
    }
}
