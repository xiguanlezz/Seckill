package com.cj.cn.service;

import com.cj.cn.pojo.Order;
import com.cj.cn.vo.GoodVO;

public interface IOrderService {
    boolean hasMiaoshaOrder(Long userId, Long goodsId);

    Order createOrder(Long userId, GoodVO goodVO);
}
