package com.cj.cn.service;

import com.cj.cn.pojo.MiaoshaGood;
import com.cj.cn.pojo.Order;
import com.cj.cn.vo.GoodVO;
import org.apache.ibatis.annotations.Param;

public interface IMiaoshaService {
    MiaoshaGood selectById(Long goodsId);

    Order doMiaosha(@Param("userId") Long userId, @Param("good") GoodVO good);
}
