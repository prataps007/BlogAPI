package com.example.blog_app_apis.services.impl;

import com.example.blog_app_apis.entities.Activity;
import com.example.blog_app_apis.entities.User;
import com.example.blog_app_apis.exceptions.ResourceNotFoundException;
import com.example.blog_app_apis.repositories.ActivityRepo;
import com.example.blog_app_apis.repositories.UserRepo;
import com.example.blog_app_apis.services.ActivityFeedService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class ActivityFeedServiceImpl implements ActivityFeedService {

    @Autowired
    private ActivityRepo activityRepo;

    @Autowired
    private UserRepo userRepository;

    public List<Activity> getActivityFeed(String userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User","user id",userId));

        // Fetch the list of user IDs that the current user follows
        Set<String> followingIds = user.getFollowingList();

        // Fetch the activities of those users
        return activityRepo.findByUserIdInOrderByTimestampDesc(followingIds);
    }
}
