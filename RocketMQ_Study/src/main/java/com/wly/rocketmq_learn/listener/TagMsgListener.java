package com.wly.rocketmq_learn.listener;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.annotation.SelectorType;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;

@Component
@RocketMQMessageListener(topic = "bootTagTopic",
        consumerGroup = "boot-tag-consumer-group",
        selectorType = SelectorType.TAG, //tag过滤模式
        selectorExpression = "tagA || tagB" )
public class TagMsgListener implements RocketMQListener<MessageExt> {
    @Override
    public void onMessage(MessageExt messageExt) {
        System.out.println(messageExt.getKeys());
        System.out.println(new String(messageExt.getBody()));
    }
}
