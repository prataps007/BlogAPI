package com.example.blog_app_apis.repositories;

import com.example.blog_app_apis.entities.Notification;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepo extends MongoRepository<Notification, String> {

    List<Notification> findByUserIdAndIsReadFalse(String userId);
}
