package com.wly.rocketmq_learn.listener;

import org.apache.rocketmq.spring.annotation.MessageModel;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;

/**
 *  CLUSTERING 集群模式下 队列会被消费者分摊 队列数量>=消费者数量 消息的消费位点 mq服务器会记录处理
 */
@Component
@RocketMQMessageListener(topic = "modeTopic",
            consumerGroup = "mode-consumer-group-a",
            messageModel = MessageModel.CLUSTERING //集群模式 负载均衡
)
public class C1 implements RocketMQListener<String> {

    @Override
    public void onMessage(String s) {
        System.out.println("我是mode-consumer-group-a组的第一个消费者" + s);
    }
}
