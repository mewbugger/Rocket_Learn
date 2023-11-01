package com.wly.seckillservice.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wly.seckillservice.domain.Order;
import com.wly.seckillservice.mapper.OrderMapper;
import com.wly.seckillservice.service.OrderService;
import org.springframework.stereotype.Service;

/**
* @author 王乐岩
* @description 针对表【order】的数据库操作Service实现
* @createDate 2023-11-01 19:45:52
*/
@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order>
    implements OrderService {

}




