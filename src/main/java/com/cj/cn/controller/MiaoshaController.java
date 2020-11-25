package com.cj.cn.controller;

import com.cj.cn.pojo.MiaoshaGood;
import com.cj.cn.pojo.Order;
import com.cj.cn.pojo.User;
import com.cj.cn.rabbitmq.MQSender;
import com.cj.cn.rabbitmq.MiaoshaMessage;
import com.cj.cn.response.CodeEnum;
import com.cj.cn.response.ResultResponse;
import com.cj.cn.service.IGoodService;
import com.cj.cn.service.IMiaoshaService;
import com.cj.cn.service.IOrderService;
import com.cj.cn.util.ConstUtil;
import com.cj.cn.util.JsonUtil;
import com.cj.cn.util.RedisUtil;
import com.cj.cn.vo.GoodVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
@RequestMapping("/miaosha")
@Controller
public class MiaoshaController {
    @Autowired
    private IMiaoshaService iMiaoshaService;
    @Autowired
    private IOrderService iOrderService;
    @Autowired
    private IGoodService iGoodService;
    @Autowired
    private MQSender mqSender;
    @Autowired
    private RedisUtil redisUtil;

    @PostConstruct
    public void preStoreStock() {
        //在对象初始化的时候将秒杀商品的库存载入Redis中
        List<MiaoshaGood> allGoodsStock = iGoodService.getAllGoodsStock();
        String allGoodsStockJsonStr = JsonUtil.objToStr(allGoodsStock);
        log.info("allGoodsStock : {}", allGoodsStockJsonStr);
        for (MiaoshaGood miaoshaGood : allGoodsStock) {
            redisUtil.set(ConstUtil.allGoodsStockKeyPrefix + miaoshaGood.getGoodsId(), miaoshaGood.getStockCount() + "", 1, TimeUnit.DAYS);
            redisUtil.set(ConstUtil.isGoodsMiaoshaOver + miaoshaGood.getGoodsId(), "false", 1, TimeUnit.DAYS);
        }
    }

    /**
     * 同步的秒杀
     */
    @RequestMapping("/do_miaosha1")
    public String miaosha1(Model model,
                           User user,
                           @RequestParam("goodsId") long goodsId) {
        /*
        model.addAttribute("user", user);
        if (user == null) {
            return "login";
        }
         */

        Long userId = 17367117439L;     //TODO Change
        userId = user.getId();
        GoodVO goods = iGoodService.getDetailById(goodsId);

        //判断库存
        int stock = goods.getStockCount();
        if (stock <= 0) {
            model.addAttribute("errmsg", CodeEnum.MIAOSHA_OVER.getMsg());
            return "miaosha_fail";
        }
        //判断是否已经秒杀到了
        boolean flag = iOrderService.hasMiaoshaOrder(userId, goodsId);
        if (flag) {
            model.addAttribute("errmsg", CodeEnum.REPEATE_MIAOSHA.getMsg());
            return "miaosha_fail";
        }
        //减库存 下订单 写入秒杀订单
        Order order = iMiaoshaService.doMiaosha(userId, goods);
        model.addAttribute("orderInfo", order);
        model.addAttribute("goods", goods);
        return "order_detail";
    }

    @RequestMapping("test")
    @ResponseBody
    public String testRabbitmq() {
        mqSender.sendMiaoshaMessage(new MiaoshaMessage().setMiaoshaUser(new User().setId(123l)).setGoodsId(1));
        return "success";
    }

    /**
     * 异步的秒杀
     */
    @RequestMapping("/do_miaosha2")
    public String miaosha2(Model model,
                           User user,
                           @RequestParam("goodsId") long goodsId) {
        /*
        model.addAttribute("user", user);
        if (user == null) {
            return "login";
        }
         */

        Long userId = 17367117439L;     //TODO Change
        userId = user.getId();

        //从Redis中预减库存
        String goodStockStr = redisUtil.get(ConstUtil.allGoodsStockKeyPrefix + goodsId);
        if (Long.parseLong(goodStockStr) <= 0) {
            redisUtil.set(ConstUtil.isGoodsMiaoshaOver + goodsId, "true", 1, TimeUnit.DAYS);
            model.addAttribute("errmsg", CodeEnum.MIAOSHA_OVER.getMsg());
            return "miaosha_fail";
        }

        //判断是否已经秒杀到了
        boolean flag = iOrderService.hasMiaoshaOrder(userId, goodsId);
        if (flag) {
            model.addAttribute("errmsg", CodeEnum.REPEATE_MIAOSHA.getMsg());
            return "miaosha_fail";
        }

        //发送一条消息到RabbitMQ中
        mqSender.sendMiaoshaMessage(new MiaoshaMessage().setGoodsId(goodsId).setMiaoshaUser(user));
        //Redis减库存
        redisUtil.decr(ConstUtil.allGoodsStockKeyPrefix + goodsId);
        return "order_detail";
    }

    @RequestMapping("result")
    public ResultResponse getMiaoshaResult(@RequestParam("goodsId") long goodsId,
                                           User user) {
        if (user == null) {
            return ResultResponse.error(CodeEnum.NO_LOGIN);
        }
        long miaoshaResult = iMiaoshaService.getMiaoshaResult(user.getId(), goodsId);
        return ResultResponse.ok(miaoshaResult);
    }

}
