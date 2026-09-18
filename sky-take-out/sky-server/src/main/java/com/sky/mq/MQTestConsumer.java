package com.sky.mq;

import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RocketMQMessageListener(topic = "spring-topic",consumerGroup = "sky-consumer-group")
public class MQTestConsumer implements RocketMQListener<String> {

    @Override
    public void onMessage(String msg){
        log.info("收到消息：{}", msg);
    }
}
