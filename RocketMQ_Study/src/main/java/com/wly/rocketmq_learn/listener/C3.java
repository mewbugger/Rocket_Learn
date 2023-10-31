package com.wly.rocketmq_learn.listener;

import org.apache.rocketmq.spring.annotation.MessageModel;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;

@Component
@RocketMQMessageListener(topic = "modeTopic",
        consumerGroup = "mode-consumer-group-a",
        messageModel = MessageModel.CLUSTERING //集群模式
)
public class C3 implements RocketMQListener<String> {
    @Override
    public void onMessage(String s) {
        System.out.println("我是mode-consumer-group-a组的第三个消费者" + s);

    }
}
