package com.sky.task;

import com.sky.websocket.WebSocketService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author zhangke
 * @version 1.0
 * @since 2025/8/2
 */

@Component
@Slf4j
public class WebSocketTask {

    @Autowired
    private WebSocketService webSocketService;

    /**
     * 每隔5秒发送一条信息给WebSocket的Client
     */
    @Scheduled(cron = "0 */5 * * * *")
    public void sendMessage() {
        webSocketService.sendToAllClient(DateTimeFormatter.ofPattern("hh:mm:ss").format(LocalDateTime.now()));
    }
}
