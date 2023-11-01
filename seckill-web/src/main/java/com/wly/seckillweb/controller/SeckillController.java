package com.wly.seckillweb.controller;

import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SeckillController {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private RocketMQTemplate rocketMQTemplate;

    /**
     * 1. 用户去重
     * 2. 库存的预扣减
     * 3. 消息放入mq
     * @param goodsId
     * @param userId
     * @return
     */
    @GetMapping("seckill/{goodsId}/{userId}")
    public String doSecKill(@PathVariable  Integer goodsId, @PathVariable  Integer userId) {
        // uk uniqueKey = userId + goodsId
        String uk = userId + "-" + goodsId;
        // setIfAbsent = setnx
        Boolean flag = stringRedisTemplate.opsForValue().setIfAbsent(uk, "");
        if (!flag) {
            return "您已经参与过该商品的抢购，请参与其他商品";
        }
        // mq 异步处理
        rocketMQTemplate.asyncSend("secKillTopic2", uk, new SendCallback(){

            @Override
            public void onSuccess(SendResult sendResult) {
                System.out.println("发送成功");
            }

            @Override
            public void onException(Throwable throwable) {
                System.out.println("发送失败" + throwable.getMessage());
            }
        });
        return "恭喜，抢购成功";
    }
}
