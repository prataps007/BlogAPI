package com.example.blog_app_apis.services.impl;

import com.example.blog_app_apis.entities.Notification;
import com.example.blog_app_apis.payloads.UserDto;
import com.example.blog_app_apis.repositories.NotificationRepo;
import com.example.blog_app_apis.services.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationServiceImpl implements NotificationService {

    @Autowired
    private NotificationRepo notificationRepo;

    public void createNotification(UserDto user, String message) {
        Notification notification = new Notification(user, message);
        notificationRepo.save(notification);
    }

    public List<Notification> getUnreadNotifications(String userId) {
        return notificationRepo.findByUserIdAndIsReadFalse(userId);
    }

    public void markAsRead(String notificationId) {
        Notification notification = notificationRepo.findById(notificationId).orElseThrow(() -> new RuntimeException("Notification not found"));
        notification.setRead(true);
        notificationRepo.save(notification);
    }
}
