package cn.xtong.example.message_answer.preset_value;

import cn.hutool.core.thread.ThreadUtil;
import cn.xtong.example.util.RabbitUtil;
import com.rabbitmq.client.Channel;

 /**
 * 预取值消费者Consumer08
 * 当Consumer08信道接受2条消息，Consumer09信道还没有接受够5条消息时，Consumer08信道暂时关闭，Consumer09接受够五条后开启
 *
 * @author 张晓童
 * @date 2023/4/2 14:23
 */
public class Consumer08 {
    public static final String QUEUE_NAME = "preset_value_queue";

    public static void main(String[] args) throws Exception {
        Channel channel = RabbitUtil.createChannel();

        System.out.println("Consumer08等待接收消息....");

        // 设置预取值
        channel.basicQos(2);
        channel.basicConsume(QUEUE_NAME, false, (consumerTag, message) -> {
            ThreadUtil.sleep(10 * 1000);
            System.out.println("消费消息：" + new String(message.getBody()));
            channel.basicAck(message.getEnvelope().getDeliveryTag(), false);
        }, (consumerTag) -> System.out.println("消息消费被中断"));
    }
}
