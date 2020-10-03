package com.cj.cn.pojo;

import javax.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Table(name = "miaosha_order")
public class MiaoshaOrder {
    @Id
    private Long id;

    /**
     * 用户ID
     */
    @Column(name = "user_id")
    private Long userId;

    /**
     * 订单ID
     */
    @Column(name = "order_id")
    private Long orderId;

    /**
     * 商品ID
     */
    @Column(name = "goods_id")
    private Long goodsId;
}