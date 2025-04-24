package com.project.shopapp.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor

public class UserChatSummaryDTO {
    private Integer userId;
    private String fullName;
    private String phoneNumber;
    private String lastMessage;
    private Date lastMessageTime;

    public UserChatSummaryDTO(Integer userId, String fullName, String phoneNumber, String lastMessage, Date lastMessageTime) {
        this.userId = userId;
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
        this.lastMessage = lastMessage;
        this.lastMessageTime = lastMessageTime;
    }
}