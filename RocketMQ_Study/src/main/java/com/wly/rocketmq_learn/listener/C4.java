package com.wly.rocketmq_learn.listener;

import org.apache.rocketmq.spring.annotation.MessageModel;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;

/**
 *  BROADCASTING 广播模式 消息会被每一个消费者都处理一次 mq服务器不会记录消息点位，也不会重试
 */
@Component
@RocketMQMessageListener(topic = "modeTopic",
        consumerGroup = "mode-consumer-group-b",
        messageModel = MessageModel.BROADCASTING //广播模式
)
public class C4 implements RocketMQListener<String> {
    @Override
    public void onMessage(String s) {
        System.out.println("我是mode-consumer-group-b组的第一个消费者" + s);
    }
}
