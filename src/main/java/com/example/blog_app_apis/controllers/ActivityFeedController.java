package com.example.blog_app_apis.controllers;

import com.example.blog_app_apis.entities.Activity;
import com.example.blog_app_apis.services.ActivityFeedService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/feed")
public class ActivityFeedController {

    @Autowired
    private ActivityFeedService activityFeedService;

    @GetMapping
    public ResponseEntity<List<Activity>> getActivityFeed(@RequestParam String userId) {
        List<Activity> activities = activityFeedService.getActivityFeed(userId);
        return ResponseEntity.ok(activities);
    }
}
