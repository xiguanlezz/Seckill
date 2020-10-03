package com.cj.cn.vo;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

@Setter
@Getter
public class GoodVO {
    private Long id;
    private String goodsName;
    private String goodsImg;
    private BigDecimal goodsPrice;
    private BigDecimal miaoshaPrice;
    private Integer stockCount;
    private Date startDate;
    private Date endDate;
}
