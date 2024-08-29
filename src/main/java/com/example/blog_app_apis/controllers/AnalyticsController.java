package com.example.blog_app_apis.controllers;

import com.example.blog_app_apis.payloads.PopularPostDto;
import com.example.blog_app_apis.payloads.UserEngagementDto;
import com.example.blog_app_apis.services.AnalyticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/analytics")
public class AnalyticsController {

    @Autowired
    private AnalyticsService analyticsService;

    @GetMapping("/user-engagement")
    public List<UserEngagementDto> getUserEngagement() {
        return analyticsService.getUserEngagement();
    }

    @GetMapping("/popular-posts")
    public List<PopularPostDto> getPopularPosts() {
        return analyticsService.getPopularPosts();
    }
}
