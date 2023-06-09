package cn.xtong.example.exchange.topic;

import cn.xtong.example.util.RabbitUtil;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * TOPIC（主题）类型交换机生产者
 * 生产者将消息发送给TOPIC模式交换机，交换机会根据RoutingKey的规则发送给指定队列。
 *
 * @author 张晓童
 * @date 2023/4/2 14:23
 */
public class Producer {
  public static final String EXCHANGE_NAME = "topic_exchange";

  public static void main(String[] args) throws Exception {
    Channel channel = RabbitUtil.createChannel();

    // 定义一个主题类型的交换机
    channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.TOPIC);

    Map<String, String> map = new HashMap<>();
    map.put("quick.orange.rabbit", "Consumer14和Consumer15");
    map.put("lazy.orange.elephant", "Consumer14和Consumer15");
    map.put("quick.orange.fox", "Consumer14");
    map.put("lazy.brown.fox", "Consumer15");
    map.put("lazy.pink.rabbit", "Consumer14或Consumer15，只有一个会接受");
    map.put("quick.brown.fox", "都不会接收到，队列接收到会被丢弃");
    map.put("quick.orange.male.rabbit", "都不会接收到，队列接收到会被丢弃");
    map.put("lazy.orange.male.rabbit", "Consumer15");

    for (Map.Entry<String, String> entry : map.entrySet()) {
      String routingKey = entry.getKey();
      String message = entry.getValue();
      channel.basicPublish(
              EXCHANGE_NAME, routingKey, null, message.getBytes(StandardCharsets.UTF_8));
    }
  }
}
