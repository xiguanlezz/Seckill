package com.cj.cn.controller;

import com.cj.cn.pojo.MiaoshaGood;
import com.cj.cn.pojo.MiaoshaOrder;
import com.cj.cn.pojo.Order;
import com.cj.cn.pojo.User;
import com.cj.cn.response.CodeEnum;
import com.cj.cn.service.IGoodService;
import com.cj.cn.service.IMiaoshaService;
import com.cj.cn.service.IOrderService;
import com.cj.cn.vo.GoodVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RequestMapping("/miaosha")
@Controller
public class MiaoshaController {
    @Autowired
    private IMiaoshaService iMiaoshaService;
    @Autowired
    private IOrderService iOrderService;
    @Autowired
    private IGoodService iGoodService;

    @RequestMapping("/do_miaosha")
    public String list(Model model, User user,
                       @RequestParam("goodsId") long goodsId) {
        model.addAttribute("user", user);
        if (user == null) {
            return "login";
        }
        Long userId = 17367117439L;     //TODO Change
        //判断库存
        GoodVO goods = iGoodService.getDetailById(goodsId);
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
}
