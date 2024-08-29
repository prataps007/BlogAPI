package com.example.blog_app_apis.services;

import com.example.blog_app_apis.entities.Activity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ActivityFeedService {

    public List<Activity> getActivityFeed(String userId);
}
