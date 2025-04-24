package com.project.shopapp.services.chat;

import com.project.shopapp.dtos.UserChatSummaryDTO;
import com.project.shopapp.models.Chat;
import com.project.shopapp.models.User;
import com.project.shopapp.repositories.ChatRepository;
import com.project.shopapp.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatRepository chatRepository;
    private final UserRepository userRepository;

    // Lấy danh sách tin nhắn giữa hai người dùng
    public List<Chat> getChatBetweenUsers(Integer senderId, Integer receiverId) {
        return chatRepository.findChatBetweenUsers(senderId, receiverId);
    }

    // Lưu tin nhắn (đã có từ trước)
    public Chat save(Chat chat) {
        return chatRepository.save(chat);
    }

    public List<UserChatSummaryDTO> getUsersChattedWithAdminSummary(Integer adminId) {
        List<Integer> userIds = chatRepository.findUserIdsChattedWithAdmin(adminId);
        List<UserChatSummaryDTO> summaries = new ArrayList<>();

        for (Integer userId : userIds) {
            // Lấy tin nhắn cuối cùng giữa admin và user
            List<Chat> chats = chatRepository.findChatBetweenUsers(adminId, userId);
            if (!chats.isEmpty()) {
                Chat lastChat = chats.get(chats.size() - 1); // Tin nhắn cuối cùng (đã sắp xếp theo thời gian)
                User user = userRepository.findById(Long.valueOf(userId)).orElse(null);
                if (user != null) {
                    summaries.add(new UserChatSummaryDTO(
                            userId,
                            user.getFullName() != null ? user.getFullName() : "",
                            user.getPhoneNumber() != null ? user.getPhoneNumber() : "",
                            lastChat.getContent() != null ? lastChat.getContent() : "",
                            lastChat.getTime()
                    ));
                }
            }
        }
        return summaries;
    }
}