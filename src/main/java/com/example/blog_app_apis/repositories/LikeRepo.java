package com.example.blog_app_apis.repositories;

import com.example.blog_app_apis.entities.Like;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LikeRepo extends MongoRepository<Like,String> {

    Optional<Like> findByUserIdAndPostId(String userId, String postId);

    Optional<Like> findByUserIdAndCommentId(String userId, String commentId);

    int countByPostIdAndLike(String postId, boolean like);

    int countByCommentIdAndLike(String commentId, boolean like);
}
