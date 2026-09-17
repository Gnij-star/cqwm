package com.sky.task;

import com.sky.websocket.WebSocketServer;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Slf4j
@Component
@RequiredArgsConstructor
public class WebSocketXxlJob {

    private final WebSocketServer webSocketServer;

    @XxlJob("webSocketBroadcastJob")
    public void sendMessageToClient() {
        String message = "这是来自服务端的消息："
                + DateTimeFormatter.ofPattern("HH:mm:ss").format(LocalDateTime.now());
        webSocketServer.sendToAllClient(message);
        log.info("WebSocket 广播消息已发送：{}", message);
    }
}