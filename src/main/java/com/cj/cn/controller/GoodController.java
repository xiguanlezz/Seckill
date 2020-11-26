package com.cj.cn.controller;

import com.cj.cn.pojo.User;
import com.cj.cn.response.ResultResponse;
import com.cj.cn.service.IGoodService;
import com.cj.cn.util.ConstUtil;
import com.cj.cn.util.JsonUtil;
import com.cj.cn.util.RedisUtil;
import com.cj.cn.vo.GoodVO;
import com.cj.cn.vo.GoodsDetailVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.concurrent.TimeUnit;

@RequestMapping("/goods")
@Controller
public class GoodController {
    @Autowired
    private IGoodService iGoodService;
    @Autowired
    private RedisUtil redisUtil;

    @RequestMapping("/to_list")
    public String list(Model model
//                       ,User user
    ) {
//        model.addAttribute("user", user);

        List<GoodVO> goodsList = null;
        //加缓存增加并发
        String goodsListJsonStr = redisUtil.get(ConstUtil.goodsListKey);
        if (StringUtils.isEmpty(goodsListJsonStr)) {
            //查询数据库并加入缓存
            goodsList = iGoodService.getList();
            goodsListJsonStr = JsonUtil.objToStr(goodsList);
            redisUtil.set(ConstUtil.goodsListKey, goodsListJsonStr, 30, TimeUnit.SECONDS);
        } else {
            //直接从缓存中取数据
            goodsListJsonStr = redisUtil.get(ConstUtil.goodsListKey);
            goodsList = JsonUtil.strToObj(goodsListJsonStr, List.class, GoodVO.class);
        }
        model.addAttribute("goodsList", goodsList);
        return "goods_list";
    }

    @RequestMapping("/to_detail/{goodsId}")
    public String detail(Model model,
//                         User user,
                         @PathVariable("goodsId") long goodsId) {
//        model.addAttribute("user", user);

        GoodVO goods = null;
        String goodVOJsonStr = "";
        String goodDetailKey = ConstUtil.goodDetailKeyPrefix + goodsId;     //商品详情在Redis中的key
        goodVOJsonStr = redisUtil.get(goodDetailKey);
        if (StringUtils.isEmpty(goodVOJsonStr)) {
            goods = iGoodService.getDetailById(goodsId);
            goodVOJsonStr = JsonUtil.objToStr(goods);
            redisUtil.set(goodDetailKey, goodVOJsonStr, 30, TimeUnit.SECONDS);
        } else {
            goodVOJsonStr = redisUtil.get(goodDetailKey);
            goods = JsonUtil.strToObj(goodVOJsonStr, GoodVO.class);
        }
        model.addAttribute("goods", goods);

        long startAt = goods.getStartDate().getTime();
        long endAt = goods.getEndDate().getTime();
        long now = System.currentTimeMillis();
        int miaoshaStatus = 0;
        int remainSeconds = 0;
        if (now < startAt) {//秒杀还没开始，倒计时
            miaoshaStatus = 0;
            remainSeconds = (int) ((startAt - now) / 1000);
        } else if (now > endAt) {//秒杀已经结束
            miaoshaStatus = 2;
            remainSeconds = -1;
        } else {//秒杀进行中
            miaoshaStatus = 1;
            remainSeconds = 0;
        }
        model.addAttribute("miaoshaStatus", miaoshaStatus);
        model.addAttribute("remainSeconds", remainSeconds);
        return "goods_detail";
    }

    @RequestMapping("/detail/{goodsId}")
    @ResponseBody
    public ResultResponse detail(HttpServletRequest request,
                                 HttpServletResponse response,
                                 User user,
                                 @PathVariable("goodsId") long goodsId) {

        GoodVO goods = iGoodService.getDetailById(goodsId);
        long startAt = goods.getStartDate().getTime();
        long endAt = goods.getEndDate().getTime();
        long now = System.currentTimeMillis();
        int miaoshaStatus = 0;
        int remainSeconds = 0;
        if (now < startAt) {
            miaoshaStatus = 0;
            remainSeconds = (int) ((startAt - now) / 1000);
        } else if (now > endAt) {
            miaoshaStatus = 2;
            remainSeconds = -1;
        } else {
            miaoshaStatus = 1;
            remainSeconds = 0;
        }
        GoodsDetailVO vo = new GoodsDetailVO()
                .setGoods(goods)
                .setUser(user)
                .setMiaoshaStatus(miaoshaStatus)
                .setRemainSeconds(remainSeconds);
        return ResultResponse.ok(vo);
    }
}
