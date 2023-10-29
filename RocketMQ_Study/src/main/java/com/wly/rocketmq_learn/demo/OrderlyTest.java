package com.wly.rocketmq_learn.demo;

import com.wly.rocketmq_learn.constant.MqConstant;
import com.wly.rocketmq_learn.domain.MsgModel;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.*;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.MessageQueueSelector;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.common.message.MessageQueue;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class OrderlyTest {

    private List<MsgModel> msgModels = Arrays.asList(
            new MsgModel("qwer", 1, "下单"),
            new MsgModel("qwer", 1, "短信"),
            new MsgModel("qwer", 1, "物流"),

            new MsgModel("zxcv", 2, "下单"),
            new MsgModel("zxcv", 2, "短信"),
            new MsgModel("zxcv", 2, "物流")
    );

    @Test
    public void orderlyProducer() throws Exception {
        DefaultMQProducer producer = new DefaultMQProducer("orderly-producer-group");
        producer.setNamesrvAddr(MqConstant.NAME_SRV_ADDR);
        producer.start();
        // 发送顺序消息 发送时要确保有序 并且要发到同一个队列下面去
        for (MsgModel msgModel : msgModels) {
            Message message = new Message("orderlyTopic", msgModel.toString().getBytes());
            try {
                // 发 相同的订单号去相同的队列
                producer.send(message, new MessageQueueSelector() {
                    @Override
                    public MessageQueue select(List<MessageQueue> msgs, Message message, Object args) {
                        // 在这里 选择队列
                        int hashCode = args.toString().hashCode();
                        int index = hashCode % msgs.size();
                        return msgs.get(index);
                    }
                }, msgModel.getOrderSn());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        producer.shutdown();
        System.out.println("发送完成");
    }

    /**
     *  并发模式
     * @throws Exception
     */
    @Test
    public void orderlyConsumer1() throws Exception {
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("orderley-consumer-group");
        consumer.setNamesrvAddr(MqConstant.NAME_SRV_ADDR);
        consumer.subscribe("orderlyTopic", "*");
        // 并发模式 多线程 如果失败 重试16次
        consumer.registerMessageListener(new MessageListenerConcurrently() {
            @Override
            public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
                System.out.println(new String(msgs.get(0).getBody()));
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            }
        });
        consumer.start();
        System.in.read();
    }

    /**
     *  顺序模式
     * @throws Exception
     */
    @Test
    public void orderlyConsumer2() throws Exception {
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("orderley-consumer-group");
        consumer.setNamesrvAddr(MqConstant.NAME_SRV_ADDR);
        consumer.subscribe("orderlyTopic", "*");
        // 并发模式 单线程 无限重试 Integer.Max_Value
        consumer.registerMessageListener(new MessageListenerOrderly() {
            @Override
            public ConsumeOrderlyStatus consumeMessage(List<MessageExt> msgs, ConsumeOrderlyContext context) {
                System.out.println("线程id：" + Thread.currentThread().getId());
                System.out.println(new String(msgs.get(0).getBody()));
                return ConsumeOrderlyStatus.SUCCESS;
            }
        });
        consumer.start();
        System.in.read();
    }
}
