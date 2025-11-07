package com.vicchen.spring.boot.websocket.stomp.controller;

import com.vicchen.spring.boot.websocket.stomp.model.ChatMessage;
import com.vicchen.spring.boot.websocket.stomp.model.MessageType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;

@Slf4j
@Controller
public class ChatController {

    @MessageMapping("/sendMessage")
    @SendTo("/topic/public")
    public ChatMessage sendMessage(@Payload ChatMessage chatMessage) {
        log.info("Sender:{} send: {}", chatMessage.getSender(), chatMessage.getContent());
        return chatMessage;
    }


    @MessageMapping("/addUser")
    @SendTo("/topic/public")
    public ChatMessage addUser(@Payload ChatMessage chatMessage, SimpMessageHeaderAccessor headerAccessor) {
        // 將使用者名稱放到 WebSocket session 裡
        headerAccessor.getSessionAttributes().put("username", chatMessage.getSender());
        chatMessage.setType(MessageType.JOIN);
        return chatMessage;

    }

}
