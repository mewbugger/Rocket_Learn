package com.wly.seckillservice.service;

import com.wly.seckillservice.domain.Goods;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author 王乐岩
* @description 针对表【goods(商品)】的数据库操作Service
* @createDate 2023-11-01 19:03:03
*/
public interface GoodsService extends IService<Goods> {


    /**
     *  真正处理秒杀的业务
     * @param userId
     * @param goodsId
     */
    void realSeckill(Integer userId, Integer goodsId);
}
