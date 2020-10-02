package com.cj.cn.pojo;

import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Table(name = "miaosha_goods")
public class MiaoshaGood {
    @Id
    private Long id;

    /**
     * 商品ID
     */
    @Column(name = "goods_id")
    private Long goodsId;

    /**
     * 秒杀价
     */
    @Column(name = "miaosha_price")
    private BigDecimal miaoshaPrice;

    /**
     * 秒杀的库存数量
     */
    @Column(name = "stock_count")
    private Integer stockCount;

    /**
     * 秒杀开始时间
     */
    @Column(name = "start_date")
    private Date startDate;

    /**
     * 秒杀结束时间
     */
    @Column(name = "end_date")
    private Date endDate;
}