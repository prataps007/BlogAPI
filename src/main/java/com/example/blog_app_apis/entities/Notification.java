package com.example.blog_app_apis.entities;

import com.example.blog_app_apis.payloads.UserDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "notifications")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Notification {

    @Id
    private String id;

    private UserDto user; // User who will receive the notification

    private String message;

    private boolean isRead;

    private LocalDateTime createdAt;

    public Notification(UserDto user, String message) {
        this.user = user;
        this.message = message;
        this.isRead = false;
        this.createdAt = LocalDateTime.now();
    }
}
