package com.cj.cn.rabbitmq;

import com.cj.cn.pojo.User;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

@Accessors(chain = true)
@Setter
@Getter
public class MiaoshaMessage {
    private User miaoshaUser;
    private long goodsId;
}
