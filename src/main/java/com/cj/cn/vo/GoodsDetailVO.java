package com.cj.cn.vo;

import com.cj.cn.pojo.User;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Accessors(chain = true)
@Setter
@Getter
public class GoodsDetailVO {
    private int miaoshaStatus = 0;
    private int remainSeconds = 0;
    private GoodVO goods;
    private User user;
}
