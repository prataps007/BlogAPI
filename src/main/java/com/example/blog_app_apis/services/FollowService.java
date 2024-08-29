package com.example.blog_app_apis.services;

import com.example.blog_app_apis.payloads.FollowDto;

public interface FollowService {

    FollowDto followUser(String userId);

    FollowDto unfollowUser(String userId);

    long countFollowers(String userId);
}
