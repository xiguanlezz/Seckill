package com.cj.cn.service.impl;

import com.cj.cn.mapper.MiaoshaGoodMapper;
import com.cj.cn.pojo.MiaoshaGood;
import com.cj.cn.pojo.Order;
import com.cj.cn.service.IGoodService;
import com.cj.cn.service.IMiaoshaService;
import com.cj.cn.service.IOrderService;
import com.cj.cn.vo.GoodVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("iMiaoshaService")
public class MiaoshaServiceImpl implements IMiaoshaService {
    @Autowired
    private MiaoshaGoodMapper miaoshaGoodMapper;

    @Autowired
    private IGoodService iGoodService;
    @Autowired
    private IOrderService iOrderService;

    @Override
    public MiaoshaGood selectById(Long goodsId) {
        return miaoshaGoodMapper.selectByPrimaryKey(goodsId);
    }

    @Transactional
    @Override
    public Order doMiaosha(Long userId, GoodVO goodVO) {
        //减库存
        iGoodService.reduceStock(goodVO);

        //下订单
        return iOrderService.createOrder(userId, goodVO);
    }
}
