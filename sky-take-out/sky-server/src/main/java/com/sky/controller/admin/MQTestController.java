package com.sky.controller.admin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class MQTestController {
    private final RocketMQTemplate rocketMQTemplate;

    @GetMapping("/mq/send")
    public String send(@RequestParam String msg){
        rocketMQTemplate.syncSend("spring-topic",msg);
        log.info("发送消息成功：{}", msg);
        return "发送成功：" + msg;
    }
}
