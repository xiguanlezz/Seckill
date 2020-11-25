package com.cj.cn.controller;

import com.cj.cn.pojo.Order;
import com.cj.cn.pojo.User;
import com.cj.cn.response.ResultResponse;
import com.cj.cn.service.IGoodService;
import com.cj.cn.service.IOrderService;
import com.cj.cn.vo.GoodVO;
import com.cj.cn.vo.OrderDetailVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/order")
@RestController
public class OrderController {
    @Autowired
    private IOrderService iOrderService;
    @Autowired
    private IGoodService iGoodService;

    @RequestMapping("detail")
    public ResultResponse info(User user,
                               @RequestParam("orderId") long orderId) {
        Order order = iOrderService.getOrderById(orderId);
        Long goodsId = order.getGoodsId();
        GoodVO goods = iGoodService.getDetailById(goodsId);
        OrderDetailVO vo = new OrderDetailVO()
                .setOrder(order)
                .setGoods(goods);
        return ResultResponse.ok(vo);
    }
}
