package com.sky.controller.admin;

import com.alibaba.fastjson.JSON;
import com.sky.websocket.WebSocketServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/test")
public class TestPushController {

    @Autowired
    private WebSocketServer webSocketServer;

    @GetMapping("/push")
    public String push() {
        Map<String, Object> msg = new HashMap<>();
        msg.put("type", 1);
        msg.put("orderId", 123);
        msg.put("content", "您有新的订单，请及时处理");
        webSocketServer.sendToAllClient(JSON.toJSONString(msg));
        return "推送成功";
    }
}