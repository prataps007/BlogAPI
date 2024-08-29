package com.example.blog_app_apis.services;

import com.example.blog_app_apis.payloads.PopularPostDto;
import com.example.blog_app_apis.payloads.UserEngagementDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface AnalyticsService {

    public List<UserEngagementDto> getUserEngagement();

    public List<PopularPostDto> getPopularPosts();
}
