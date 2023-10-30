package com.wly.rocketmq_learn.listener;

import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;

@Component
@RocketMQMessageListener(topic = "bootTestTopic", consumerGroup = "boot-test-consumer-group")
public class BootSimpleMsgListener implements RocketMQListener<MessageExt> {


    /***
     *  这个方法就是消费者方法
     *  如果泛型指定了固定的类型，那么消费体就是我们的参数
     *  MessageExt 类型是消息的所有内容
     *
     *  -----------
     *  没有报错 就是签收了
     *  如果报错了 就是拒收 就会重试
     * @param message
     */
    @Override
    public void onMessage(MessageExt message) {
        System.out.println(new String(message.getBody()));
    }
}
