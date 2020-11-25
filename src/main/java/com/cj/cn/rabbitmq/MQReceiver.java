package com.cj.cn.rabbitmq;

import com.cj.cn.service.IMiaoshaService;
import com.cj.cn.util.JsonUtil;
import com.cj.cn.vo.GoodVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RabbitListener(queuesToDeclare = @Queue("miaosha.queue"))
public class MQReceiver {
    @Autowired
    private IMiaoshaService iMiaoshaService;

    @RabbitHandler
    public void receive(String msg) {
        log.info("receive message : {}", msg);
        MiaoshaMessage miaoshaMessage = JsonUtil.strToObj(msg, MiaoshaMessage.class);
        Long userId = miaoshaMessage.getMiaoshaUser().getId();
        long goodsId = miaoshaMessage.getGoodsId();
        iMiaoshaService.doMiaosha(userId, new GoodVO().setId(goodsId));
    }
}
