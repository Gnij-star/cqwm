package com.sky.websocket;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
@ServerEndpoint("/ws/{sid}")
public class WebSocketServer {

    // 1. 用 ConcurrentHashMap 保证线程安全
    private static final Map<String, Session> SESSION_MAP = new ConcurrentHashMap<>();

    @OnOpen
    public void onOpen(Session session, @PathParam("sid") String sid) {
        SESSION_MAP.put(sid, session);
        log.info("客户端 {} 建立连接，当前在线数：{}", sid, SESSION_MAP.size());
    }

    @OnMessage
    public void onMessage(String message, @PathParam("sid") String sid) {
        log.info("收到来自客户端 {} 的消息：{}", sid, message);
    }

    @OnClose
    public void onClose(@PathParam("sid") String sid) {
        SESSION_MAP.remove(sid);
        log.info("连接断开：{}，当前在线数：{}", sid, SESSION_MAP.size());
    }

    // 2. 新增 @OnError，处理异常
    @OnError
    public void onError(Session session, Throwable error, @PathParam("sid") String sid) {
        log.error("WebSocket 异常, sid={}", sid, error);
        SESSION_MAP.remove(sid);
    }

    // 3. 异步发送 + 清理僵尸连接
    public void sendToAllClient(String message) {
        SESSION_MAP.forEach((sid, session) -> {
            if (session.isOpen()) {
                session.getAsyncRemote().sendText(message);
            } else {
                SESSION_MAP.remove(sid);
                log.warn("清理僵尸连接：{}", sid);
            }
        });
    }
}