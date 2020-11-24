package com.cj.cn.rabbitmq;

import com.cj.cn.util.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class MQSender {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void sendMiaoshaMessage(MiaoshaMessage miaoshaMessage) {
        String msg = JsonUtil.objToStr(miaoshaMessage);
        log.info("send message : {}", msg);
        rabbitTemplate.convertAndSend("", MQConfig.MIAOSHA_QUEUE, msg);
    }
}
