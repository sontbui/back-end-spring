package com.project.shopapp.dtos;


import com.project.shopapp.models.Chat;

import java.time.LocalDateTime;
import java.util.Date;

public class ChatDTO {
    private Integer id;
    private Long senderId;
    private Long receiverId;

    private String content;
    private Date time;


    // Constructor mặc định
    public ChatDTO() {
    }

    // Constructor để ánh xạ từ entity Chat
    public ChatDTO(Chat chat) {
        this.id = chat.getId();
        this.senderId = chat.getSender()!= null ? chat.getSender().getId() : null;
        this.receiverId = chat.getReceiver()!= null ? chat.getReceiver().getId(): null;

        this.content = chat.getContent();
        this.time = chat.getTime();

    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Long getSenderId() {
        return senderId;
    }

    public void setSenderId(Long senderId) {
        this.senderId = senderId;
    }

    public Long getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(Long receiverId) {
        this.receiverId = receiverId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }
}