package com.example.blog_app_apis.services;

import com.example.blog_app_apis.entities.Notification;
import com.example.blog_app_apis.payloads.UserDto;

import java.util.List;

public interface NotificationService {

    void createNotification(UserDto user, String message);

    List<Notification> getUnreadNotifications(String userId);

    void markAsRead(String notificationId);
}
