package com.example.blog_app_apis.services;

import com.example.blog_app_apis.payloads.CommentDto;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public interface CommentService {

    CommentDto createComment(CommentDto commentDto,String postId);
    void deleteComment(String commentId);

    List<CommentDto> getCommentsByPostId(String postId);

    // CommentDto getCommentById(String commentId);
}
