package cn.xtong.example.dead_letter.message_rejected;

import cn.xtong.example.util.RabbitUtil;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * 死信队列消息被拒绝生产者
 *
 * @author： 张晓童
 * @date： 2023/4/2 15:50
 */
public class Producer {

    // 普通交换机名称
    public static final String NORMAL_EXCHANGE_NAME = "message_rejected_normal_exchange";
    // 死信交换机名称
    public static final String DEAD_EXCHANGE_NAME = "message_rejected_dead_exchange";
    // 普通队列名称
    public static final String NORMAL_QUEUE_NAME = "message_rejected_normal_queue";

    public static void main(String[] args) throws Exception {
        Channel channel = RabbitUtil.createChannel();

        channel.exchangeDeclare(NORMAL_EXCHANGE_NAME, BuiltinExchangeType.DIRECT);

        // 普通队列的参数
        Map<String, Object> arguments = new HashMap<>();
        arguments.put("x-dead-letter-exchange", DEAD_EXCHANGE_NAME);
        arguments.put("x-dead-letter-routing-key", "lisi");

        channel.queueDeclare(NORMAL_QUEUE_NAME, false, false, false, arguments);

        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            String message = scanner.next();
            channel.basicPublish(NORMAL_EXCHANGE_NAME, "zhangsan", null, message.getBytes(StandardCharsets.UTF_8));
            System.out.println("成功发送消息：" + message);
        }
    }
}
