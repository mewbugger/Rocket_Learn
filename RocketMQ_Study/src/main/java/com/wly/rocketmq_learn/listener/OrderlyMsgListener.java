package com.wly.rocketmq_learn.listener;


import com.alibaba.fastjson.JSON;
import com.wly.rocketmq_learn.domain.MsgModel;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.spring.annotation.ConsumeMode;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;

@Component
@RocketMQMessageListener(topic = "bootOrderlyTopic",
        consumerGroup = "boot-orderly-consumer-group",
        consumeMode = ConsumeMode.ORDERLY, // 顺序消费模式 单线程
        maxReconsumeTimes = 5 )
public class OrderlyMsgListener implements RocketMQListener<MessageExt> {

    @Override
    public void onMessage(MessageExt messageExt) {
        MsgModel msgModel = JSON.parseObject(new String(messageExt.getBody()), MsgModel.class);
        System.out.println(msgModel);
    }
}
