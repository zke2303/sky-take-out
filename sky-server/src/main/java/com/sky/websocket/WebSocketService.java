package com.sky.websocket;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * @author zhangke
 * @version 1.0
 * @description
 * @since 2025/8/2
 */
@Component
@ServerEndpoint(value = "/ws/{sid}")
@Slf4j
public class WebSocketService {
    // 存放会话对象
    public static Map<String, Session> sessionMap = new HashMap();

    /**
     * 建立WebSocket连接
     * @param session
     * @param sid
     */
    @OnOpen
    public void onOpen(Session session, @PathParam(value = "sid") String sid) {
        log.info("与客户端 {} 建立连接", sid);
        sessionMap.put(sid, session);
    }

    /**
     * 关闭与客户端的连接
     * @param session
     * @param sid
     */
    @OnClose
    public void onClose(Session session, @PathParam(value = "sid") String sid) {
        log.info("与客户端 {} 关闭连接", sid);
        sessionMap.remove(sid);
    }

    /**
     * 接送客户端发送过来的信息
     * @param session
     * @param sid
     */
    @OnMessage
    public void onMessage(Session session, @PathParam(value = "sid") String sid) {
        log.info("收到来自客户端{}发来的信息：{}", sid, session);
    }

    /**
     * 服务端群发信息给所有的客户端
     *
     */
    public void sendToAllClient(String msg) {
        Collection<Session> sessions = sessionMap.values();
        if (!sessions.isEmpty()) {
            for (Session session : sessions) {
                try {
                    session.getBasicRemote().sendText(msg);
                }catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}
