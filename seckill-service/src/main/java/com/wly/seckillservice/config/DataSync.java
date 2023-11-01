package com.wly.seckillservice.config;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wly.seckillservice.domain.Goods;
import com.wly.seckillservice.mapper.GoodsMapper;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import javax.annotation.PostConstruct;
import java.util.List;

/**
 *  1. 每天10点 晚上8点 通过定时任务 将mysql的库存 同步到redis中去
 *  2. 为了测试方便 希望项目启动的时候 就数据同步
 */
@Component
public class DataSync {

    @Autowired
    private GoodsMapper goodsMapper;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    /**
     *  希望这个方法在项目启动以后
     *  并且在这个类的属性注入完毕以后执行
     *
     */
    @PostConstruct
    public void initData () {
        QueryWrapper<Goods> queryWrapper = new QueryWrapper<>();
        List<Goods> goodsList = goodsMapper.selectList(queryWrapper);
        if (CollectionUtils.isEmpty(goodsList)) {
            return;
        }
        goodsList.forEach(goods -> {
            stringRedisTemplate.opsForValue().set("goodsId:" + goods.getGoodsId(), goods.getTotalStocks().toString());
        });
    }
}
