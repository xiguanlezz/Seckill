package com.cj.cn.vo;

import com.cj.cn.pojo.Order;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Accessors(chain = true)
@Setter
@Getter
public class OrderDetailVO {
    private GoodVO goods;
    private Order order;
}
