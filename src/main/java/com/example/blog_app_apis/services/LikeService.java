package com.example.blog_app_apis.services;

import com.example.blog_app_apis.payloads.LikeDto;

public interface LikeService {

    LikeDto likePost(String postId);

    LikeDto dislikePost(String postId);

    LikeDto likeComment(String commentId);

    LikeDto dislikeComment(String commentId);

    int countLikesForPost(String postId);

    int countDislikesForPost(String postId);

    int countLikesForComment(String commentId);

    int countDislikesForComment(String commentId);
}
