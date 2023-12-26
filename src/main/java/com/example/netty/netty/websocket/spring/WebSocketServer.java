package com.example.netty.netty.websocket.spring;

import io.netty.util.internal.ConcurrentSet;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@ServerEndpoint("/ws")
@Component
public class WebSocketServer {

    /**
     * 在线人数
     */
    private static AtomicInteger atomicInteger = new AtomicInteger();
    /**
     * 用来保存每个客户端对应的WebSocketServer实例
     */
    private static ConcurrentSet<WebSocketServer> webSocketSet = new ConcurrentSet<>();
    /**
     * 每个客户端对应的连接会话，通过它来主动给客户端发送消息
     */
    private Session session;


    /**
     * 当连接建立成功之后，会调用该方法
     *
     * @param session
     */
    @OnOpen
    public void onOpen(Session session) {
        this.session = session;
        webSocketSet.add(this);
        atomicInteger.incrementAndGet();
        log.info("新用户上线，当前在线人数：{}", atomicInteger.get());
    }

    @OnClose
    public void OnClose(Session session) {
        webSocketSet.remove(this);
        atomicInteger.decrementAndGet();
        log.info("新用户下线，当前在线人数：{}", atomicInteger.get());
    }

    @OnMessage
    public void OnMessage(String message, Session session) throws IOException {
        for (WebSocketServer webSocketServer : webSocketSet) {
            webSocketServer.session.getBasicRemote().sendText(session.getId() + " 说：" + message);
        }
    }


}
