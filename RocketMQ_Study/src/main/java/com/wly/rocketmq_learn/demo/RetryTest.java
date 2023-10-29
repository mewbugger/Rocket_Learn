package com.wly.rocketmq_learn.demo;

import com.wly.rocketmq_learn.constant.MqConstant;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;
import org.junit.Test;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class RetryTest {

    @Test
    public void retryProducer() throws Exception {
        DefaultMQProducer producer = new DefaultMQProducer("retry-producer-group");
        producer.setNamesrvAddr(MqConstant.NAME_SRV_ADDR);
        producer.start();
        // 生产者发送消息， 重试次数
        producer.setRetryTimesWhenSendFailed(2);
        producer.setRetryTimesWhenSendAsyncFailed(2);
        String key = UUID.randomUUID().toString();
        System.out.println(key);
        Message message = new Message("retryTopic", "vip1", key, "我是vip1的文章".getBytes());
        producer.send(message);
        System.out.println("发送成功");
        producer.shutdown();
    }

    /**
     *  重试的时间间隔 默认重试16次
     *      可以自定义重试次数
     *      如果重试了16次（并发模式） int最大值（顺序模式）都没成功，则是一个死信消息，会放到一个死信主题中
     *      重试次数一般是5次
     *
     * @throws Exception
     */
    @Test
    public void retryConsumer() throws Exception {
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("retry-consumer-group");
        consumer.setNamesrvAddr(MqConstant.NAME_SRV_ADDR);
        consumer.subscribe("retryTopic", "*");
        //设置重试次数
        consumer.setMaxReconsumeTimes(3);
        consumer.registerMessageListener(new MessageListenerConcurrently() {
            @Override
            public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
                MessageExt messageExt = msgs.get(0);
                System.out.println(new Date());
                System.out.println(new String(messageExt.getBody()));
                // 业务报错了 返回null 返回 RECONSUME_later 都会重试
                return ConsumeConcurrentlyStatus.RECONSUME_LATER;
            }
        });
        consumer.start();
        System.in.read();
    }

    @Test
    public void retryDeadConsumer() throws Exception {
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("retry-dead-consumer-group");
        consumer.setNamesrvAddr(MqConstant.NAME_SRV_ADDR);
        consumer.subscribe("%DLQ%retry-consumer-group", "*");
        //设置重试次数
        consumer.setMaxReconsumeTimes(3);
        consumer.registerMessageListener(new MessageListenerConcurrently() {
            @Override
            public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
                MessageExt messageExt = msgs.get(0);
                System.out.println(new Date());
                System.out.println("记录到特别为止 文件 mysql 通知人工处理");
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            }
        });
        consumer.start();
        System.in.read();
    }


    @Test
    public void retryConsumer2() throws Exception {
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("retry-consumer-group");
        consumer.setNamesrvAddr(MqConstant.NAME_SRV_ADDR);
        consumer.subscribe("retryTopic", "*");
        //设置重试次数
        consumer.setMaxReconsumeTimes(3);
        consumer.registerMessageListener(new MessageListenerConcurrently() {
            @Override
            public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
                MessageExt messageExt = msgs.get(0);
                System.out.println(new Date());
                // 业务处理
                try {
                    handleDB();
                } catch (Exception e) {
                    // 重试
                    int reconsumeTimes = messageExt.getReconsumeTimes();
                    if (reconsumeTimes >= 3) {
                        // 不要重试了
                        System.out.println("记录到特别为止 文件 mysql 通知人工处理");
                        return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
                    }
                    return ConsumeConcurrentlyStatus.RECONSUME_LATER;
                }
                System.out.println(new String(messageExt.getBody()));
                // 业务报错了 返回null 返回 RECONSUME_later 都会重试
                return ConsumeConcurrentlyStatus.RECONSUME_LATER;
            }
        });
        consumer.start();
        System.in.read();
    }

    private void handleDB() {
        int i = 10 / 0;
    }
}
