package com.example.blog_app_apis.services.impl;

import com.example.blog_app_apis.entities.Activity;
import com.example.blog_app_apis.entities.Comment;
import com.example.blog_app_apis.entities.Post;
import com.example.blog_app_apis.entities.User;
import com.example.blog_app_apis.exceptions.ResourceNotFoundException;
import com.example.blog_app_apis.payloads.CommentDto;
import com.example.blog_app_apis.repositories.ActivityRepo;
import com.example.blog_app_apis.repositories.CommentRepo;
import com.example.blog_app_apis.repositories.PostRepo;
import com.example.blog_app_apis.repositories.UserRepo;
import com.example.blog_app_apis.services.CommentService;
import com.example.blog_app_apis.services.SecurityService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    private PostRepo postRepo;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private CommentRepo commentRepo;

    @Autowired
    private SecurityService securityService; // Inject the SecurityService


    @Autowired
    private ActivityRepo activityRepo;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public CommentDto createComment(CommentDto commentDto, String postId) { //  String userId

        if (commentDto.getContent() == null || commentDto.getContent().isEmpty()) {
            throw new IllegalArgumentException("Comment content cannot be empty");
        }

        User user = securityService.getCurrentUser();  // Use the utility method to get the current user

        Post post = postRepo.findById(postId).orElseThrow(() -> new ResourceNotFoundException("Post","post id",postId));

        Comment comment = modelMapper.map(commentDto, Comment.class);
        comment.setPost(post);
        comment.setUser(user);

        // logging statement
        System.out.println("Creating comment for postId: " + postId);
        System.out.println("Comment created by userId: " + user.getEmail());

        Comment savedComment = commentRepo.save(comment);

        // Create an activity record
        Activity activity = new Activity();
        activity.setUserId(user.getId());
        activity.setAction("CREATE_COMMENT");
        activity.setType("COMMENT");
        activity.setTargetId(savedComment.getId());
        activity.setTimestamp(LocalDateTime.now());

        activityRepo.save(activity);

        // Add comment to post
        post.getComments().add(savedComment);
        postRepo.save(post);

        // add comment to user
        user.getComments().add(savedComment);
        userRepo.save(user);

        return modelMapper.map(savedComment,CommentDto.class);
    }

    // only admin or the user who created the comment can delete the comment
    @Override
    public void deleteComment(String commentId) {
        Comment com = commentRepo.findById(commentId).orElseThrow(() -> new ResourceNotFoundException("Comment","comment id",commentId));
        // delete comment from posts too
        // Get the post associated with the comment
        Post post = com.getPost();

        // Remove the comment from the post's comments list
        post.getComments().remove(com);
        postRepo.save(post); // Save the post to update its comments list

        // Get the user associated with the comment
        User user = com.getUser();

        // Remove the comment from the user's comments list
        user.getComments().remove(com);
        userRepo.save(user);

        commentRepo.delete(com);
    }

    // Additional method to check if the current user is the owner of the comment
    public boolean isCommentOwner(String commentId) {
        User currentUser = securityService.getCurrentUser();  // Use the utility method to get the current user
        Comment comment = commentRepo.findById(commentId)
                .orElseThrow(() -> new ResourceNotFoundException("Comment", "comment id", commentId));
        return comment.getUser().getId().equals(currentUser.getId());

    }


    // get comment by post id - with details like user who crated the comment
    @Override
    public List<CommentDto> getCommentsByPostId(String postId) {

        Post post = postRepo.findById(postId).orElseThrow(() -> new ResourceNotFoundException("Post","post id", postId));

        List<Comment> comments = post.getComments();

        List<CommentDto> commentDtos = comments.stream().map((comment) -> (modelMapper.map(comment, CommentDto.class))).collect(Collectors.toList());

        return commentDtos;
    }


}
