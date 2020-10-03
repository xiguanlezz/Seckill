package com.cj.cn.service;

import com.cj.cn.pojo.MiaoshaGood;
import com.cj.cn.vo.GoodVO;

import java.util.List;

public interface IGoodService {
    /**
     * 查询秒杀商品列表
     */
    List<GoodVO> getList();

    /**
     * 查询秒杀商品的详情
     */
    GoodVO getDetailById(long goodsId);

    /**
     * 减少库存
     */
    void reduceStock(GoodVO goodVO);
}
