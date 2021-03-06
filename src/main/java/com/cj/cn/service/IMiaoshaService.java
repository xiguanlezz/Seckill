package com.cj.cn.service;

import com.cj.cn.pojo.MiaoshaGood;
import com.cj.cn.pojo.Order;
import com.cj.cn.pojo.User;
import com.cj.cn.vo.GoodVO;
import org.apache.ibatis.annotations.Param;

import java.awt.image.BufferedImage;

public interface IMiaoshaService {
    MiaoshaGood selectById(Long goodsId);

    Order doMiaosha(@Param("userId") Long userId, @Param("good") GoodVO good);

    //0表示秒杀进行中, -1表示秒杀失败, 正数直接就是订单号
    long getMiaoshaResult(long userId, long goodsId);

    BufferedImage createVerifyCode(User user, long goodsId);

    boolean checkVerifyCode(Long userId, long goodsId, int verifyCode);

    String createMiaoshaPath(User user, long goodsId);
}
