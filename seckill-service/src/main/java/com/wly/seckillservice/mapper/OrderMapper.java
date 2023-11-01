package com.wly.seckillservice.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import com.wly.seckillservice.domain.Order;
import org.apache.ibatis.annotations.Mapper;

/**
* @author 王乐岩
* @description 针对表【order】的数据库操作Mapper
* @createDate 2023-11-01 19:45:52
* @Entity generator.domain.Order
*/
@Mapper
public interface OrderMapper extends BaseMapper<Order> {

}




