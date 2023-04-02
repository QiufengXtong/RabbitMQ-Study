package cn.xtong.example.release_confirmation.batch;

import cn.xtong.example.util.RabbitUtil;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.MessageProperties;

/**
 * 批量发布确认生产者
 * 发送消息10000个，耗时：1256ms
 * 吞吐量高，出现问题，无法定位到具体的消息（不推荐使用）
 *
 * @author 张晓童
 * @date 2023/4/2 14:23
 */
public class Producer {

    // 发送10000条消息
    private static final Integer MESSAGE_COUNT = 10000;
    // 1000次确认一次
    private static final Integer CONFIRMATION_COUNT = 1000;

    public static final String QUEUE_NAME = "patch_release_confirmation_queue";

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

            if (i % CONFIRMATION_COUNT == 0) {
                // 发布确认：true确认成功发布消息，false，不确认成功发布消息
                channel.waitForConfirms();
                System.out.println("成功发送消息");
            }
        }
        // 结束时间戳
        long end = System.currentTimeMillis();
        System.out.println("批量发布确认，发送消息" + MESSAGE_COUNT + "个，耗时：" + (end - begin) + "ms");
    }
}
