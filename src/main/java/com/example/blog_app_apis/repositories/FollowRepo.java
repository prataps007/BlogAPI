package com.example.blog_app_apis.repositories;

import com.example.blog_app_apis.entities.Follow;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FollowRepo extends MongoRepository<Follow,String> {

    // follow
    boolean existsByFollowerIdAndFollowedId(String followerId, String followedId);

    // unfollow
    void deleteByFollowerIdAndFollowedId(String followerId, String followedId);

    long countByFollowedId(String followedId);  // For counting followers
}
