package com.cj.cn;

import com.cj.cn.pojo.User;
import com.cj.cn.rabbitmq.MQSender;
import com.cj.cn.rabbitmq.MiaoshaMessage;
import com.cj.cn.service.IGoodService;
import com.cj.cn.util.ConstUtil;
import com.cj.cn.util.RedisUtil;
import com.cj.cn.vo.GoodVO;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class MiaoshaApplicationTests {
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private IGoodService iGoodService;
    @Autowired
    private MQSender mqSender;

    @Test
    void testGoodsListJson() {
        System.out.println(redisUtil.get(ConstUtil.goodsListKey));
    }

    @Test
    void testMiaoshaOne() {
        GoodVO goodVO = new GoodVO();
        goodVO.setId(1l);
        int i = iGoodService.reduceStock(goodVO);
        Assert.assertEquals(1, i);
    }

    @Test
    void testRabbitmq() {
        mqSender.sendMiaoshaMessage(new MiaoshaMessage().setMiaoshaUser(new User().setId(123l)).setGoodsId(1));
    }

}
