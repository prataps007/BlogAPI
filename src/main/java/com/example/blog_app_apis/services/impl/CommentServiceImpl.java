package com.example.blog_app_apis.services.impl;

import com.example.blog_app_apis.entities.Comment;
import com.example.blog_app_apis.entities.Post;
import com.example.blog_app_apis.exceptions.ResourceNotFoundException;
import com.example.blog_app_apis.payloads.CommentDto;
import com.example.blog_app_apis.repositories.CommentRepo;
import com.example.blog_app_apis.repositories.PostRepo;
import com.example.blog_app_apis.services.CommentService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    private PostRepo postRepo;

    @Autowired
    private CommentRepo commentRepo;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public CommentDto createComment(CommentDto commentDto, String postId) {

        if (commentDto.getContent() == null || commentDto.getContent().isEmpty()) {
            throw new IllegalArgumentException("Comment content cannot be empty");
        }

        Post post = postRepo.findById(postId).orElseThrow(() -> new ResourceNotFoundException("Post","post id",postId));
        Comment comment = modelMapper.map(commentDto, Comment.class);
        comment.setPost(post);

        // logging statement
        System.out.println("Creating comment for postId: " + postId);

        Comment savedComment = commentRepo.save(comment);

        // Add comment to post
        post.getComments().add(savedComment);
        postRepo.save(post);

        return modelMapper.map(savedComment,CommentDto.class);
    }

    @Override
    public void deleteComment(String commentId) {
        Comment com = commentRepo.findById(commentId).orElseThrow(() -> new ResourceNotFoundException("Comment","comment id",commentId));
        commentRepo.delete(com);
    }

    @Override
    public List<CommentDto> getCommentsByPostId(String postId) {

        Post post = postRepo.findById(postId).orElseThrow(() -> new ResourceNotFoundException("Post","post id", postId));

        List<Comment> comments = post.getComments();

        List<CommentDto> commentDtos = comments.stream().map((comment) -> (modelMapper.map(comment, CommentDto.class))).collect(Collectors.toList());

        return commentDtos;
    }
}
