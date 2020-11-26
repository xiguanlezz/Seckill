package com.cj.cn;

import com.cj.cn.dao.MiaoshaGoodMapper;
import com.cj.cn.pojo.MiaoshaGood;
import com.cj.cn.pojo.User;
import com.cj.cn.rabbitmq.MQSender;
import com.cj.cn.rabbitmq.MiaoshaMessage;
import com.cj.cn.service.IGoodService;
import com.cj.cn.util.ConstUtil;
import com.cj.cn.util.JsonUtil;
import com.cj.cn.util.RedisUtil;
import com.cj.cn.vo.GoodVO;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import tk.mybatis.mapper.entity.Example;

import java.util.List;
import java.util.concurrent.TimeUnit;

@SpringBootTest
class MiaoshaApplicationTests {
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private IGoodService iGoodService;
    @Autowired
    private MQSender mqSender;
    @Autowired
    private MiaoshaGoodMapper miaoshaGoodMapper;

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

    @Test
    void testMapper() {
        Example example = new Example(MiaoshaGood.class);
        example.selectProperties("goodsId", "stockCount");
        List<MiaoshaGood> miaoshaGoods = miaoshaGoodMapper.selectByExample(example);
        System.out.println(JsonUtil.objToStr(miaoshaGoods));
    }

    @Test
    void testRedisDecr() {
        System.out.println("pre : " + redisUtil.get(ConstUtil.allGoodsStockKeyPrefix + 1));
        System.out.println(redisUtil.decr(ConstUtil.allGoodsStockKeyPrefix + 1));
        System.out.println("after : " + redisUtil.get(ConstUtil.allGoodsStockKeyPrefix + 1));
    }

    @Test
    void testRedis() {
        redisUtil.set("miaosha_verfify_code_17367117439_1", "0", 365, TimeUnit.DAYS);
    }
}
