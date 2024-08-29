package com.example.blog_app_apis.controllers;

import com.example.blog_app_apis.entities.Comment;
import com.example.blog_app_apis.entities.Notification;
import com.example.blog_app_apis.entities.User;
import com.example.blog_app_apis.exceptions.ResourceNotFoundException;
import com.example.blog_app_apis.payloads.UserDto;
import com.example.blog_app_apis.repositories.UserRepo;
import com.example.blog_app_apis.services.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import org.springframework.security.core.annotation.AuthenticationPrincipal;


import java.util.List;

@RestController
@RequestMapping("/api/")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private UserRepo userRepo;

    @GetMapping("/notifications/unread")
    public ResponseEntity<List<Notification>> getUnreadNotifications() {
        User currentUser = getCurrentUser();  // Use the utility method to get the current user

        List<Notification> notifications = notificationService.getUnreadNotifications(currentUser.getId());
        return new ResponseEntity<>(notifications, HttpStatus.OK);
    }


    @PostMapping("/notifications/{notificationId}/read")
    public ResponseEntity<String> markNotificationAsRead(@PathVariable String notificationId) {
        notificationService.markAsRead(notificationId);
        return new ResponseEntity<>("Notification marked as read !!",HttpStatus.NO_CONTENT);
    }

    private User getCurrentUser() {
        // Extract the username from the security context
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        // Fetch the user from the database using the username
        return userRepo.findByEmail(username)
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", username));
    }
}
