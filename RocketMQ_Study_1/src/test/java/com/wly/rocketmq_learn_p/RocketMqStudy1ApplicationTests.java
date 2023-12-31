package com.wly.rocketmq_learn_p;

import com.alibaba.fastjson2.JSON;
import com.wly.rocketmq_learn_p.domain.MsgModel;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.apache.rocketmq.spring.support.RocketMQHeaders;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;

import java.util.Arrays;
import java.util.List;

@SpringBootTest
class RocketMqStudy1ApplicationTests {


    @Autowired
    private RocketMQTemplate rocketMQTemplate;

    @Test
    void contextLoads() {
        /*// 同步
        rocketMQTemplate.syncSend("bootTestTopic", "我是boot的一条消息");

        // 异步
        rocketMQTemplate.asyncSend("bootTestTopic", "我是一个异步消息", new SendCallback() {
            @Override
            public void onSuccess(SendResult sendResult) {
                System.out.println("成功");
            }

            @Override
            public void onException(Throwable throwable) {
                System.out.println("失败" + throwable.getMessage());
            }
        });

        // 单向
        rocketMQTemplate.sendOneWay("bootOnewayTopic", "单向消息");

        // 延迟
        Message<String> msg = MessageBuilder.withPayload("我是一个延迟消息").build();
        rocketMQTemplate.syncSend("bootMsTopic", msg, 3000, 3);*/

        // 顺序消息 发送者方 需要将一组消息 都发送在同一个队列中 消费者 需要单线程消费

        List<MsgModel> msgModels = Arrays.asList(
                new MsgModel("qwer", 1, "下单"),
                new MsgModel("qwer", 1, "短信"),
                new MsgModel("qwer", 1, "物流"),

                new MsgModel("zxcv", 2, "下单"),
                new MsgModel("zxcv", 2, "短信"),
                new MsgModel("zxcv", 2, "物流")
        );
        msgModels.forEach(msgModel -> {
            // 发送 一般都是以json的方式进行处理
            rocketMQTemplate.syncSendOrderly("bootOrderlyTopic", JSON.toJSONString(msgModel), msgModel.getOrderSn());
        });
    }

    @Test
    void testKeyTest() throws Exception {
        //rocketMQTemplate.syncSend("bootTagTopic:tagA", "我是一个带tag的消息");

        // key是携带在消息头的
        Message<String> msg
                = MessageBuilder.withPayload("我是一个带key的消息").setHeader(RocketMQHeaders.KEYS, "keys").build();
        rocketMQTemplate.syncSend("bootKeyTopic", msg );

    }


    /**
     * 测试消息消费模式 集群模式
     * @throws Exception
     */
    @Test
    void modeTest() throws Exception {
        for (int i = 0; i < 5; i++) {
            rocketMQTemplate.syncSend("modeTopic", "我是第" + i + "个消息");

        }
    }

}
