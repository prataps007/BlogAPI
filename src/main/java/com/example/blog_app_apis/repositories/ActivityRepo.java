package com.example.blog_app_apis.repositories;

import com.example.blog_app_apis.entities.Activity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface ActivityRepo extends MongoRepository<Activity, String> {

    // for user activity feed
    List<Activity> findByUserIdInOrderByTimestampDesc(Set<String> userIds);  // sort in descending order --> latest activity first

    // for user Activity Logging and Analytics
    //List<Activity> findByUserId(String userId);
    List<Activity> findByAction(String action);
}

