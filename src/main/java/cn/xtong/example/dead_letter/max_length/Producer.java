package cn.xtong.example.dead_letter.max_length;

import cn.xtong.example.util.RabbitUtil;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * 死信队列最大长度生产者
 *
 * @author： 张晓童
 * @date： 2023/4/2 15:50
 */
public class Producer {

    // 普通交换机名称
    public static final String NORMAL_EXCHANGE_NAME = "max_length_normal_exchange";
    // 死信交换机名称
    public static final String DEAD_EXCHANGE_NAME = "max_length_dead_exchange";
    // 普通队列名称
    public static final String NORMAL_QUEUE_NAME = "max_length_normal_queue";

    public static void main(String[] args) throws Exception {
        Channel channel = RabbitUtil.createChannel();

        channel.exchangeDeclare(NORMAL_EXCHANGE_NAME, BuiltinExchangeType.DIRECT);

        // 普通队列的参数
        Map<String, Object> arguments = new HashMap<>();
        arguments.put("x-dead-letter-exchange", DEAD_EXCHANGE_NAME);
        arguments.put("x-dead-letter-routing-key", "lisi");
        // 设置普通队列消息最大长度
        arguments.put("x-max-length", 5);

        channel.queueDeclare(NORMAL_QUEUE_NAME, false, false, false, arguments);

        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            String message = scanner.next();
            channel.basicPublish(NORMAL_EXCHANGE_NAME, "zhangsan", null, message.getBytes(StandardCharsets.UTF_8));
            System.out.println("成功发送消息：" + message);
        }
    }
}
