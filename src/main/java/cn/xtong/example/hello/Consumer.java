package cn.xtong.example.hello;

import cn.xtong.example.util.RabbitUtil;
import com.rabbitmq.client.Channel;

/**
 * hello world 消费者
 *
 * @author 张晓童
 * @date 2023/4/2 14:23
 */
public class Consumer {
    // 队列名称
    public static final String QUEUE_NAME = "hello";

    public static void main(String[] args) throws Exception {
        // 创建信道
        Channel channel = RabbitUtil.createChannel();

        System.out.println("等待接收消息....");

        /**
         * 消费者消费消息
         * 1. 队列名称
         * 2. 接受到消息是否应答，true应答，false不应答
         * 3. 成功接收消息回调函数
         * 4. 消费者取消消息回调
         */
        channel.basicConsume(QUEUE_NAME, true,
                (consumerTag, message) -> System.out.println("消费消息：" + new String(message.getBody())),
                consumerTag -> System.out.println("消息被取消"));
    }
}
