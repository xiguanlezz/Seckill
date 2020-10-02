package com.cj.cn.pojo;

import java.math.BigDecimal;
import javax.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Table(name = "goods")
public class Good {
    /**
     * 商品ID
     */
    @Id
    private Long id;

    /**
     * 商品名称
     */
    @Column(name = "goods_name")
    private String goodsName;

    /**
     * 商品标题
     */
    @Column(name = "goods_title")
    private String goodsTitle;

    /**
     * 商品的图片
     */
    @Column(name = "goods_img")
    private String goodsImg;

    /**
     * 商品单价
     */
    @Column(name = "goods_price")
    private BigDecimal goodsPrice;

    /**
     * 商品库存, -1表示没有限制
     */
    @Column(name = "goods_stock")
    private Integer goodsStock;

    /**
     * 商品的详细介绍
     */
    @Column(name = "goods_detail")
    private String goodsDetail;
}