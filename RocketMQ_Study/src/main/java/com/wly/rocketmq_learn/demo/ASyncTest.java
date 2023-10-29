package com.wly.rocketmq_learn.demo;

import com.wly.rocketmq_learn.constant.MqConstant;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.junit.Test;

/**
 *  异步测试
 */
public class ASyncTest {

    @Test
    public void asyncProducer() throws Exception {
        DefaultMQProducer producer = new DefaultMQProducer("async-producer-test");
        producer.setNamesrvAddr(MqConstant.NAME_SRV_ADDR);
        producer.start();
        Message message = new Message("asyncTopic", "我是一个异步消息".getBytes());
        producer.send(message, new SendCallback() {
            @Override
            public void onSuccess(SendResult sendResult) {
                System.out.println("发送成功");
            }

            @Override
            public void onException(Throwable e) {
                System.err.println("发送失败" + e.getMessage());
            }
        });
        //由于SendCallback是异步执行的，所以可能send完message后程序停了，还没来得及回调，所以挂起，查看回调函数的结果
        System.out.println("我先执行");
        System.in.read();
    }
}
