package com.wly.seckillservice.listener;


import com.wly.seckillservice.service.GoodsService;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.spring.annotation.ConsumeMode;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
@RocketMQMessageListener(topic = "secKillTopic2",
        consumerGroup = "seckill-consumer-group",
        consumeThreadNumber = 40,
        consumeMode = ConsumeMode.CONCURRENTLY)
public class SeckillListener implements RocketMQListener<MessageExt> {

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    final int ZX_TIME = 10000;

    /**
     *  扣减库存
     *  写订单表
     *  redis setnx
     * @param messageExt
     */
    @Override
    public void onMessage(MessageExt messageExt) {
        String msg = new String(messageExt.getBody());
        // userId + "-" + goodsId
        Integer userId = Integer.parseInt(msg.split("-")[0]);
        Integer goodsId = Integer.parseInt(msg.split("-")[1]);

        int currentThreadTime = 0;
        while (currentThreadTime < ZX_TIME) {
            Boolean flag = stringRedisTemplate.opsForValue().setIfAbsent("lock:" + goodsId, "", Duration.ofSeconds(30));
            if (flag) {
                // 拿到锁成功
                try {
                    goodsService.realSeckill(userId, goodsId);
                    return;
                } finally {
                    // 释放锁
                    stringRedisTemplate.delete("lock:" + goodsId);
                }

            } else {
                // 没拿到锁 自旋
                currentThreadTime += 200;
                try {
                    Thread.sleep(200L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

    }
}
