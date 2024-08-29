package com.example.blog_app_apis.services.impl;

import com.example.blog_app_apis.entities.*;
import com.example.blog_app_apis.exceptions.ResourceNotFoundException;
import com.example.blog_app_apis.payloads.LikeDto;
import com.example.blog_app_apis.repositories.*;
import com.example.blog_app_apis.services.LikeService;
import com.example.blog_app_apis.services.SecurityService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Optional;

@Service
public class LikeServiceImpl implements LikeService {

    @Autowired
    private LikeRepo likeRepo;

    @Autowired
    private PostRepo postRepo;

    @Autowired
    private CommentRepo commentRepo;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private SecurityService securityService; // Inject the SecurityService

    @Autowired
    private ActivityRepo activityRepo;

    @Override
    public LikeDto likePost(String postId) {
        Post post = postRepo.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post", "post id", postId));

        User currUser = securityService.getCurrentUser(); // Use SecurityService
        String userId = currUser.getId();

        // Check if the user has already liked or disliked the post
        Like existingLike = likeRepo.findByUserIdAndPostId(userId, postId).orElse(null);

        if (existingLike != null) {
            if (!existingLike.isLike()) {
                // Remove the old like/dislike from the post's likes collection
                Like finalExistingLike = existingLike;
                post.getLikes().removeIf(like -> like.getId().equals(finalExistingLike.getId()));

                // User had previously disliked the post, so change it to like
                existingLike.setLike(true);
            } else {
                // If the post was already liked, do nothing
                return modelMapper.map(existingLike, LikeDto.class);
            }
        } else {
            // No existing like or dislike, so create a new like
            existingLike = new Like(null, userId, postId, null, true);
        }

        likeRepo.save(existingLike);
        post.getLikes().add(existingLike); // Add the updated like back to the post
        postRepo.save(post);

        // Create an activity record
        Activity activity = new Activity();
        activity.setUserId(userId);
        activity.setAction("LIKE_POST");
        activity.setType("POST");
        activity.setTargetId(post.getPostId());
        activity.setTimestamp(LocalDateTime.now());

        // Log before saving activity
        //System.out.println("Saving activity for post like by user: " + userId);

        activityRepo.save(activity);

        // Log after saving activity
        //System.out.println("Activity saved successfully for post like by user: " + userId);

        return modelMapper.map(existingLike, LikeDto.class);
    }

    @Override
    public LikeDto dislikePost(String postId) {
        Post post = postRepo.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post", "post id", postId));

        User currUser = securityService.getCurrentUser(); // Use SecurityService
        String userId = currUser.getId();

        // Check if the user has already liked or disliked the post
        Like existingLike = likeRepo.findByUserIdAndPostId(userId, postId).orElse(null);

        if (existingLike != null) {
            if (existingLike.isLike()) {
                Like finalExistingLike = existingLike;
                post.getLikes().removeIf(like -> like.getId().equals(finalExistingLike.getId()));
                // User had previously liked the post, so change it to dislike
                existingLike.setLike(false);
            }
            else {
                // If the post was already liked, do nothing
                return modelMapper.map(existingLike, LikeDto.class);
            }
        } else {
            // No existing like or dislike, so create a new dislike
            existingLike = new Like(null, userId, postId, null, false);
        }

        likeRepo.save(existingLike);
        post.getLikes().add(existingLike); // Add the updated dislike back to the post
        postRepo.save(post);

        // Create an activity record
        Activity activity = new Activity();
        activity.setUserId(userId);
        activity.setAction("DISLIKE_POST");
        activity.setType("POST");
        activity.setTargetId(post.getPostId());
        activity.setTimestamp(LocalDateTime.now());

        activityRepo.save(activity);

        return modelMapper.map(existingLike, LikeDto.class);
    }


    @Override
    public LikeDto likeComment(String commentId) {
        Comment comment = commentRepo.findById(commentId)
                .orElseThrow(() -> new ResourceNotFoundException("comment", "comment id", commentId));

        User currUser = securityService.getCurrentUser(); // Use SecurityService
        String userId = currUser.getId();

        // Check if the user has already liked or disliked the comment
        Like existingLike = likeRepo.findByUserIdAndCommentId(userId, commentId).orElse(null);

        if (existingLike != null) {
            if (!existingLike.isLike()) {
                // Remove the old like/dislike from the comment's likes collection
                Like finalExistingLike = existingLike;
                comment.getLikes().removeIf(like -> like.getId().equals(finalExistingLike.getId()));

                // User had previously disliked the comment, so change it to like
                existingLike.setLike(true);
                likeRepo.save(existingLike);
                comment.getLikes().add(existingLike); // Add the updated like back to the comment
            }
            else {
                // If the post was already liked, do nothing
                return modelMapper.map(existingLike, LikeDto.class);
            }
        } else {
            // No existing like or dislike, so create a new like
            existingLike = new Like(null, userId, null, commentId, true);
            likeRepo.save(existingLike);
            comment.getLikes().add(existingLike);
        }

        // Create an activity record
        Activity activity = new Activity();
        activity.setUserId(userId);
        activity.setAction("LIKE_COMMENT");
        activity.setType("COMMENT");
        activity.setTargetId(comment.getId());
        activity.setTimestamp(LocalDateTime.now());

        activityRepo.save(activity);


        commentRepo.save(comment);
        return modelMapper.map(existingLike, LikeDto.class);
    }

    @Override
    public LikeDto dislikeComment(String commentId) {
        Comment comment = commentRepo.findById(commentId)
                .orElseThrow(() -> new ResourceNotFoundException("comment", "comment id", commentId));

        User currUser = securityService.getCurrentUser(); // Use SecurityService
        String userId = currUser.getId();

        // Check if the user has already liked or disliked the comment
        Like existingLike = likeRepo.findByUserIdAndCommentId(userId, commentId).orElse(null);

        if (existingLike != null) {
            if (existingLike.isLike()) {
                // Remove the old like/dislike from the comment's likes collection
                Like finalExistingLike = existingLike;
                comment.getLikes().removeIf(like -> like.getId().equals(finalExistingLike.getId()));

                // User had previously liked the comment, so change it to dislike
                existingLike.setLike(false);
                comment.getLikes().add(existingLike);
                likeRepo.save(existingLike);
            } else {
                // If the comment was already disliked, do nothing
                return modelMapper.map(existingLike, LikeDto.class);
            }
        } else {
            // No existing like or dislike, so create a new dislike
            existingLike = new Like(null, userId, null, commentId, false);
            likeRepo.save(existingLike);
            comment.getLikes().add(existingLike);
        }

        // Create an activity record
        Activity activity = new Activity();
        activity.setUserId(userId);
        activity.setAction("DISLIKE_COMMENT");
        activity.setType("COMMENT");
        activity.setTargetId(comment.getId());
        activity.setTimestamp(LocalDateTime.now());

        activityRepo.save(activity);

        commentRepo.save(comment);
        return modelMapper.map(existingLike, LikeDto.class);
    }


    @Override
    public int countLikesForPost(String postId) {
        Post post = postRepo.findById(postId).orElseThrow(() -> new ResourceNotFoundException("post","post id", postId));

        //int count = post.getLikes().size(); //likeRepo.countByPostIdAndLike(postId, true);
        int likeCount = (int) post.getLikes().stream()
                .filter(Like::isLike)
                .count();
        System.out.println("Likes count for post " + postId + ": " + likeCount);
        return likeCount;
    }

    @Override
    public int countDislikesForPost(String postId) {
        Post post = postRepo.findById(postId).orElseThrow(() -> new ResourceNotFoundException("post","post id", postId));

        int dislikeCount = (int) post.getLikes().stream()
                .filter(like -> !like.isLike())  // Filter for dislikes
                .count();

        System.out.println("Dislikes count for post " + postId + ": " + dislikeCount);
        return dislikeCount;
    }

    @Override
    public int countLikesForComment(String commentId) {
        Comment comment = commentRepo.findById(commentId)
                .orElseThrow(() -> new ResourceNotFoundException("comment", "comment id", commentId));

        int likeCount = (int) comment.getLikes().stream()
                .filter(Like::isLike)  // Filter for likes
                .count();

        System.out.println("Likes count for comment " + commentId + ": " + likeCount);
        return likeCount;
    }


    @Override
    public int countDislikesForComment(String commentId) {
        Comment comment = commentRepo.findById(commentId)
                .orElseThrow(() -> new ResourceNotFoundException("comment", "comment id", commentId));

        int dislikeCount = (int) comment.getLikes().stream()
                .filter(like -> !like.isLike())  // Filter for dislikes
                .count();

        System.out.println("Dislikes count for comment " + commentId + ": " + dislikeCount);
        return dislikeCount;
    }

}
