package com.wly.seckillweb.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SeckillController {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 1. 用户去重
     * 2. 库存的预扣减
     * 3. 消息放入mq
     * @param goodsId
     * @param userId
     * @return
     */
    @GetMapping("seckill")
    public String doSecKill(Integer goodsId, Integer userId) {
        // uk uniqueKey = userId + goodsId
        String uk = userId + "-" + goodsId;
        // setIfAbsent = setnx
        Boolean flag = stringRedisTemplate.opsForValue().setIfAbsent(uk, "");
        if (!flag) {
            return "您已经参与过该商品的抢购，请参与其他商品";
        }

        return "恭喜，抢购成功";
    }
}
