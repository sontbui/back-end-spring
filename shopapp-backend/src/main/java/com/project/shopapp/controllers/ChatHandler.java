package com.project.shopapp.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.shopapp.dtos.ChatDTO;
import com.project.shopapp.models.Chat;
import com.project.shopapp.models.User;
import com.project.shopapp.services.chat.ChatService;
import com.project.shopapp.services.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
public class ChatHandler extends TextWebSocketHandler {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final List<WebSocketSession> sessions = new ArrayList<>();
    @Autowired
    private ChatService chatService;

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        sessions.add(session);
        System.out.println("New session connected: " + session.getId());
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        // Parse JSON thành ChatDTO
        ChatDTO chatMessage = objectMapper.readValue(message.getPayload(), ChatDTO.class);
        System.out.println("Received message from client: " + chatMessage.getContent());
        // Chuyển đổi ChatDTO thành Chat entity để lưu vào DB
        Chat chatEntity = Chat.builder()
                .sender(User.builder().id(chatMessage.getSenderId()).build())
                .receiver(User.builder().id(chatMessage.getReceiverId()).build())
                .content(chatMessage.getContent())
                .time(chatMessage.getTime())
                .build();

        // Lưu tin nhắn vào cơ sở dữ liệu
        chatEntity.setTime(new Date());
        Chat savedChat = chatService.save(chatEntity);

        // Chuyển đổi lại thành ChatDTO để gửi
        ChatDTO savedMessage = new ChatDTO(savedChat);
        String responseMessage = objectMapper.writeValueAsString(savedMessage);

        // Gửi tin nhắn đến các client khác
        for (WebSocketSession s : sessions) {
            if (s.isOpen() && !s.equals(session)) {
                s.sendMessage(new TextMessage(responseMessage));
            }
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, org.springframework.web.socket.CloseStatus status) throws Exception {
        sessions.remove(session);
        System.out.println("Session disconnected: " + session.getId());
    }
}