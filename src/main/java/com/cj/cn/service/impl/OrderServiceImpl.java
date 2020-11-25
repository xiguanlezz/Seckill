package com.cj.cn.service.impl;

import com.cj.cn.dao.MiaoshaOrderMapper;
import com.cj.cn.dao.OrderMapper;
import com.cj.cn.pojo.MiaoshaOrder;
import com.cj.cn.pojo.Order;
import com.cj.cn.service.IOrderService;
import com.cj.cn.vo.GoodVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;

@Service("iOrderService")
public class OrderServiceImpl implements IOrderService {
    @Autowired
    private MiaoshaOrderMapper miaoshaOrderMapper;
    @Autowired
    private OrderMapper orderMapper;

    @Override
    public boolean hasMiaoshaOrder(Long userId, Long goodsId) {
        Example example = new Example(MiaoshaOrder.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("userId", userId).andEqualTo("goodsId", goodsId);
        MiaoshaOrder miaoshaOrder = miaoshaOrderMapper.selectOneByExample(example);
        return miaoshaOrder != null;
    }

    @Transactional
    @Override
    public Order createOrder(Long userId, GoodVO goodVO) {
        //创建普通订单
        Order order = new Order();
        order.setCreateDate(new Date());
        order.setDeliveryAddrId(0L);
        order.setGoodsCount(1);
        order.setGoodsId(goodVO.getId());
        order.setGoodsName(goodVO.getGoodsName());
        order.setGoodsPrice(goodVO.getMiaoshaPrice());
        order.setOrderChannel((byte) 1);
        order.setStatus((byte) 0);
        order.setUserId(userId);
        orderMapper.insert(order);

        //创建秒杀订单
        long orderId = order.getId();
        MiaoshaOrder miaoshaOrder = new MiaoshaOrder();
        miaoshaOrder.setGoodsId(goodVO.getId());
        miaoshaOrder.setOrderId(orderId);
        miaoshaOrder.setUserId(userId);
        miaoshaOrderMapper.insert(miaoshaOrder);
        return order;
    }
}
