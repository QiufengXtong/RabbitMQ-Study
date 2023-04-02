package cn.xtong.example.message_answer.unfair_distribute;

import cn.xtong.example.util.RabbitUtil;
import com.rabbitmq.client.Channel;

import java.nio.charset.StandardCharsets;
import java.util.Scanner;

/**
 * 不公平分发生产者
 *
 * @author 张晓童
 * @date 2023/4/2 14:23
 */
public class Producer {
    public static final String QUEUE_NAME = "unfair_distribute_queue";

    public static void main(String[] args) throws Exception {
        Channel channel = RabbitUtil.createChannel();

        channel.queueDeclare(QUEUE_NAME, false, false, false, null);

        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            String message = scanner.next();
            channel.basicPublish("", QUEUE_NAME, null, message.getBytes(StandardCharsets.UTF_8));
            System.out.println("成功发送消息："+message);
        }
    }
}
