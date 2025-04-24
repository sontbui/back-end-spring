package com.project.shopapp.repositories;

import com.project.shopapp.dtos.UserChatSummaryDTO;
import com.project.shopapp.models.Chat;
import com.project.shopapp.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ChatRepository extends JpaRepository<Chat, Integer> {

    // Truy vấn danh sách tin nhắn giữa sender và receiver (hoặc ngược lại)
    @Query("SELECT c FROM Chat c WHERE " +
           "(c.sender.id = :senderId AND c.receiver.id = :receiverId) OR " +
           "(c.sender.id = :receiverId AND c.receiver.id = :senderId) " +
           "ORDER BY c.time ASC")
    List<Chat> findChatBetweenUsers(@Param("senderId") Integer senderId, @Param("receiverId") Integer receiverId);

    @Query("SELECT DISTINCT CASE WHEN c.sender.id = :adminId THEN c.receiver.id ELSE c.sender.id END " +
            "FROM Chat c " +
            "WHERE c.sender.id = :adminId OR c.receiver.id = :adminId")
    List<Integer> findUserIdsChattedWithAdmin(@Param("adminId") Integer adminId);
}