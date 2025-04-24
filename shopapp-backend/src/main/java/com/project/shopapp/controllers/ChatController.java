package com.project.shopapp.controllers;

import com.project.shopapp.dtos.ChatDTO;
import com.project.shopapp.dtos.UserChatSummaryDTO;
import com.project.shopapp.models.Chat;

import com.project.shopapp.models.User;
import com.project.shopapp.services.chat.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("${api.prefix}/chat")
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;

    // API lấy danh sách tin nhắn giữa hai người dùng
    @GetMapping("/history")
    public ResponseEntity<List<ChatDTO>> getChatHistory(
            @RequestParam("senderId") Integer senderId,
            @RequestParam("receiverId") Integer receiverId) {
        List<Chat> chatHistory = chatService.getChatBetweenUsers(senderId, receiverId);
        // Chuyển đổi từ List<Chat> sang List<ChatDTO>
        List<ChatDTO> chatDTOs = chatHistory.stream()
                .map(ChatDTO::new) // Sử dụng constructor ChatDTO(Chat chat)
                .collect(Collectors.toList());
        return ResponseEntity.ok(chatDTOs);
    }

    // API lấy danh sách user và tin nhắn cuối cùng với admin
    @GetMapping("/users-chatted-summary")
    public ResponseEntity<List<UserChatSummaryDTO>> getUsersChattedWithAdminSummary(
            @RequestParam("adminId") Integer adminId) {
        List<UserChatSummaryDTO> summaries = chatService.getUsersChattedWithAdminSummary(adminId);
        return ResponseEntity.ok(summaries);
    }
}