package com.cj.cn.service.impl;

import com.cj.cn.dao.MiaoshaGoodMapper;
import com.cj.cn.exception.GlobalException;
import com.cj.cn.pojo.MiaoshaGood;
import com.cj.cn.pojo.Order;
import com.cj.cn.pojo.User;
import com.cj.cn.response.CodeEnum;
import com.cj.cn.service.IGoodService;
import com.cj.cn.service.IMiaoshaService;
import com.cj.cn.service.IOrderService;
import com.cj.cn.util.ConstUtil;
import com.cj.cn.util.RedisUtil;
import com.cj.cn.vo.GoodVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service("iMiaoshaService")
public class MiaoshaServiceImpl implements IMiaoshaService {
    @Autowired
    private MiaoshaGoodMapper miaoshaGoodMapper;
    @Autowired
    private IGoodService iGoodService;
    @Autowired
    private IOrderService iOrderService;
    @Autowired
    private RedisUtil redisUtil;
    private static final char[] ops;

    static {
        ops = new char[]{'+', '-', '*'};
    }

    @Override
    public MiaoshaGood selectById(Long goodsId) {
        return miaoshaGoodMapper.selectByPrimaryKey(goodsId);
    }

    @Transactional
    @Override
    public Order doMiaosha(Long userId, GoodVO goodVO) {
        //减库存
        int count = iGoodService.reduceStock(goodVO);
        if (count < 1) {
            throw new GlobalException(CodeEnum.MIAOSHA_OVER);
        }
        //下订单
        return iOrderService.createOrder(userId, goodVO);
    }

    @Override
    public long getMiaoshaResult(long userId, long goodsId) {
        Order miaoshaOrder = iOrderService.getMiaoshaOrder(userId, goodsId);
        if (miaoshaOrder != null) {
            //秒杀成功返回订单号
            return miaoshaOrder.getId();
        }
        String flag = redisUtil.get(ConstUtil.isGoodsMiaoshaOver + goodsId);
        if ("true".equals(flag)) {
            //商品已经秒杀完毕
            return -1;
        } else {
            //商品还在秒杀中
            return 0;
        }
    }

    @Override
    public BufferedImage createVerifyCode(User user, long goodsId) {
        if (user == null || goodsId <= 0) {
            return null;
        }
        int width = 80;
        int height = 32;
        //创建图片
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics g = image.getGraphics();

        //设置背景颜色
        g.setColor(new Color(0xDCDCDC));
        g.fillRect(0, 0, width, height);

        //画出边界
        g.setColor(Color.black);
        g.drawRect(0, 0, width - 1, height - 1);

        //随机生成实例的数字
        Random rdm = new Random();

        //制作一些噪点
        for (int i = 0; i < 50; i++) {
            int x = rdm.nextInt(width);
            int y = rdm.nextInt(height);
            g.drawOval(x, y, 0, 0);
        }

        //生成随机的运算式
        String verifyCode = generateVerifyCode(rdm);
        g.setColor(new Color(0, 100, 0));
        g.setFont(new Font("Candara", Font.BOLD, 24));
        g.drawString(verifyCode, 8, 24);

        //清除
        g.dispose();

        //把验证码存到redis中
        int result = calculateExpression(verifyCode);
        redisUtil.set(ConstUtil.verifyCodeKeyPrefix + user.getId() + "_" + goodsId, result + "", 5, TimeUnit.SECONDS);

        //仅仅测试限流使用
//        redisUtil.set(ConstUtil.verifyCodeKeyPrefix + user.getId() + "_" + goodsId, 1 + "", 365, TimeUnit.DAYS);

        //输出图片
        return image;
    }

    private String generateVerifyCode(Random rdm) {
        int num1 = rdm.nextInt(10);
        int num2 = rdm.nextInt(10);
        int num3 = rdm.nextInt(10);
        char op1 = ops[rdm.nextInt(3)];
        char op2 = ops[rdm.nextInt(3)];
        return "" + num1 + op1 + num2 + op2 + num3;
    }

    private int calculateExpression(String exp) {
        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine engine = manager.getEngineByName("JavaScript");
        try {
            return (int) engine.eval(exp);
        } catch (ScriptException e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public boolean checkVerifyCode(Long userId, long goodsId, int verifyCode) {
        String numStr = redisUtil.get(ConstUtil.verifyCodeKeyPrefix + userId + "_" + goodsId);
        return verifyCode == Integer.parseInt(numStr);
    }

    @Override
    public String createMiaoshaPath(User user, long goodsId) {
        String path = UUID.randomUUID().toString() + "963";
        redisUtil.set(ConstUtil.miaoShaPathKeyPrefix + user.getId() + "_" + goodsId, path, 30, TimeUnit.SECONDS);
        return path;
    }
}
