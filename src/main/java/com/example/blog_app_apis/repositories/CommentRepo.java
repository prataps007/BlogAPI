package com.example.blog_app_apis.repositories;

import com.example.blog_app_apis.entities.Comment;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepo extends MongoRepository<Comment, String> {


}
