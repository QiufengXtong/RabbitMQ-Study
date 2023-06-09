package cn.xtong.example.exchange.direct;

import cn.xtong.example.util.RabbitUtil;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;

import java.nio.charset.StandardCharsets;
import java.util.Scanner;

/**
 * DIRECT（直接）类型交换机生产者
 * 生产者将消息发送给DIRECT类型交换机，交换机会将消息发送给指定RoutingKey的队列。
 *
 * @author 张晓童
 * @date 2023/4/2 14:23
 */
public class Producer {
    public static final String EXCHANGE_NAME = "direct_exchange";

    public static void main(String[] args) throws Exception {
        Channel channel = RabbitUtil.createChannel();

        // 定义一个直接类型的交换机
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT);

        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            String message = scanner.next();
            channel.basicPublish(EXCHANGE_NAME, "error", null, message.getBytes(StandardCharsets.UTF_8));
            System.out.println("成功发送消息："+message);
        }
    }
}
