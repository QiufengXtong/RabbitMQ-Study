package cn.xtong.example.release_confirmation.async;

import cn.xtong.example.util.RabbitUtil;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConfirmCallback;

import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;

/**
 * 生产者
 * 异步发布确认，发送消息10000个，耗时：1089ms
 * 最佳性能和资源使用，出现错误可以很好控制（推荐使用）
 * 核心思想：监控消息是否成功确认，成功确认的话走消息确认成功回调，否则走消息确认失败回调。
 * 如何获取消息确认失败的数据：声明线程安全有序的哈希表，在发送消息时，把消息存入，在消息成功确认回调种删除成功确认数据。
 */
public class Producer {

    // 发送10000条消息
    private static final Integer MESSAGE_COUNT = 10000;

    public static final String QUEUE_NAME = "async_release_confirmation_queue";

    public static void main(String[] args) throws Exception {
        Channel channel = RabbitUtil.createChannel();

        channel.queueDeclare(QUEUE_NAME, true, false, false, null);

        // 开启发布确认
        channel.confirmSelect();
        // 开始时间戳
        long begin = System.currentTimeMillis();

        /**
         * 线程安全有序的哈希表，适用于高并发情况下
         * 1. 轻松将序号和消息进行关联
         * 2. 轻松批量删除条目，只要给到序号
         * 3. 支持高并发
         */
        ConcurrentSkipListMap<Long,String> concurrentSkipListMap = new ConcurrentSkipListMap<>();

        /**
         * 消息确认成功回调
         * 1. 消息的Tag
         * 2. 是否批量确认，true批量确认，false不是批量确认
         */
        ConfirmCallback ackCallback = (deliveryTag, multiply) -> {
            if (multiply){
                ConcurrentNavigableMap<Long,String> concurrentNavigableMap = concurrentSkipListMap.headMap(deliveryTag+1);
                System.out.println("批量确认消息："+concurrentNavigableMap);
                concurrentNavigableMap.clear();
            }else{
                System.out.println("单个确认消息："+concurrentSkipListMap.headMap(deliveryTag+1));
                concurrentSkipListMap.remove(deliveryTag);
            }

            if (concurrentSkipListMap.size() == 0){
                System.out.println("所有消息确认成功发送");
            }
        };

        /**
         * 消息确认失败回调
         * 1. 消息的Tag
         * 2. 是否批量确认，true批量确认，false不是批量确认
         */
        ConfirmCallback nackCallback = (deliveryTag, multiply) -> {
            System.out.println("确认失败的消息：" + concurrentSkipListMap.headMap(deliveryTag+1));
        };

        /**
         * 监听消息发送成功失败
         * 1. 成功回调
         * 2. 失败回调
         */
        channel.addConfirmListener(ackCallback, nackCallback);

        for (Integer i = 0; i < MESSAGE_COUNT; i++) {
            String message = "消息" + (i+1);
            concurrentSkipListMap.put(channel.getNextPublishSeqNo(), message);
            channel.basicPublish("", QUEUE_NAME, null, message.getBytes());

        }

        // 结束时间戳
        long end = System.currentTimeMillis();
        System.out.println("异步发布确认，发送消息" + MESSAGE_COUNT + "个，耗时：" + (end - begin) + "ms");
    }
}
