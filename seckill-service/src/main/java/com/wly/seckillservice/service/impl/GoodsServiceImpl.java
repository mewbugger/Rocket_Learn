package com.wly.seckillservice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wly.seckillservice.domain.Goods;
import com.wly.seckillservice.domain.Order;
import com.wly.seckillservice.mapper.GoodsMapper;
import com.wly.seckillservice.mapper.OrderMapper;
import com.wly.seckillservice.service.GoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
* @author 王乐岩
* @description 针对表【goods(商品)】的数据库操作Service实现
* @createDate 2023-11-01 19:03:03
*/
@Service
public class GoodsServiceImpl extends ServiceImpl<GoodsMapper, Goods>
    implements GoodsService {
    @Autowired
    private GoodsMapper goodsMapper;

    @Autowired
    private OrderMapper orderMapper;

    /**
     *  扣减库存 -1
     *  写订单表
     * @param userId
     * @param goodsId
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void realSeckill(Integer userId, Integer goodsId) {
        QueryWrapper<Goods> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("goods_id", goodsId);
        Goods goods = goodsMapper.selectOne(queryWrapper);
        int finalStock = goods.getTotalStocks() - 1;
        if (finalStock < 0) {
            throw new RuntimeException("商品" + goodsId + "库存不足，用户id为：" + userId);
        }
        goods.setTotalStocks(finalStock);
        goods.setUpdateTime(new Date());
        int result = goodsMapper.updateById(goods);
        if (result > 0) {
            Order order = new Order();
            order.setGoodsid(goodsId);
            order.setUserid(userId);
            order.setCreatetime(new Date());
            int insertResult = orderMapper.insert(order);
        }
    }
}




