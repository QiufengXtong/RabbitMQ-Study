package cn.xtong.example.release_confirmation.single;

import cn.xtong.example.util.RabbitUtil;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.MessageProperties;

/**
 * 生产者
 * 单个发布确认，发送消息10000个，耗时：206084ms
 */
public class Producer {

    // 发送10000条消息
    private static final Integer MESSAGE_COUNT = 10000;

    public static final String QUEUE_NAME = "single_release_confirmation_queue";

    public static void main(String[] args) throws Exception {
        Channel channel = RabbitUtil.createChannel();

        channel.queueDeclare(QUEUE_NAME, true, false, false, null);

        // 开启发布确认
        channel.confirmSelect();
        // 开始时间戳
        long begin = System.currentTimeMillis();

        for (Integer i = 0; i < MESSAGE_COUNT; i++) {
            String message = i + "";
            channel.basicPublish("", QUEUE_NAME, MessageProperties.PERSISTENT_TEXT_PLAIN, message.getBytes());

            // 发布确认：true确认成功发布消息，false，不确认成功发布消息
            boolean flag = channel.waitForConfirms();
            if (flag) {
                System.out.println("成功发送消息：" + message);
            }
        }
        // 结束时间戳
        long end = System.currentTimeMillis();
        System.out.println("单个发布确认，发送消息" + MESSAGE_COUNT + "个，耗时：" + (end - begin) + "ms");
    }
}
