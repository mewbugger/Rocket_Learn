package com.wly.rocketmq_learn.demo;


import com.wly.rocketmq_learn.constant.MqConstant;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.common.message.Message;
import org.junit.Test;

/**
 *  单向发送消息测试
 */
public class OnewayTest {

    @Test
    public void onewayProducer() throws Exception {
        DefaultMQProducer producer = new DefaultMQProducer("oneway-producer-group");
        producer.setNamesrvAddr(MqConstant.NAME_SRV_ADDR);
        producer.start();
        Message message = new Message("onewayTopic", "日志xxx".getBytes());
        producer.sendOneway(message);
        System.out.println("成功发送");
        producer.shutdown();
    }
}
