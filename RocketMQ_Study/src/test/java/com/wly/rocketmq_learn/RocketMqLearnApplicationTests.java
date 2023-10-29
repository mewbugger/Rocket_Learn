package com.wly.rocketmq_learn;

import com.wly.rocketmq_learn.constant.MqConstant;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class RocketMqLearnApplicationTests {

    @Test
    void contextLoads() {
    }

    /**
     *  发消息
     */
    @Test
    void testSendMsg() throws Exception {
        // 创建一个生产者（指定一个组名）
        DefaultMQProducer producer = new DefaultMQProducer("test-producer-group");
        // 连接namesrv
        producer.setNamesrvAddr(MqConstant.NAME_SRV_ADDR);
        // 启动
        producer.start();
        // 创建一个小希
        Message message = new Message("testTopic", "我是一个简单的消息".getBytes());
        // 发送消息
        SendResult sendResult = producer.send(message);
        // 关闭生产者
        producer.shutdown();
    }

}
