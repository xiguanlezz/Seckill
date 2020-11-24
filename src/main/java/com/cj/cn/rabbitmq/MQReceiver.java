package com.cj.cn.rabbitmq;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RabbitListener(queuesToDeclare = @Queue("miaosha.queue"))
public class MQReceiver {

    @RabbitHandler
    public void receive(String msg) {
        log.info("receive message : {}", msg);
    }
}
