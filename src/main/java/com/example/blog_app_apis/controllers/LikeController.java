package com.example.blog_app_apis.controllers;

import com.example.blog_app_apis.entities.User;
import com.example.blog_app_apis.exceptions.ResourceNotFoundException;
import com.example.blog_app_apis.payloads.LikeDto;
import com.example.blog_app_apis.payloads.UserDto;
import com.example.blog_app_apis.repositories.UserRepo;
import com.example.blog_app_apis.services.LikeService;
import com.example.blog_app_apis.services.NotificationService;
import com.example.blog_app_apis.services.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/likes")
public class LikeController {

    @Autowired
    private LikeService likeService;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private PostService postService;

    @Autowired
    private NotificationService notificationService;

    @PostMapping("/posts/{postId}/like")
    public ResponseEntity<?> likePost(@PathVariable String postId) {

        LikeDto likeDto = likeService.likePost(postId);

        // Get the post author's user (assuming postService can provide it)
        UserDto postAuthor = postService.getPostById(postId).getUser();

        // Create a notification
        notificationService.createNotification(postAuthor, "A new like on your post.");

        //String successMessage = "You liked post with ID: " + postId + " successfully.";

        return new ResponseEntity<>(likeDto, HttpStatus.CREATED);
    }

    @PostMapping("/posts/{postId}/dislike")
    public ResponseEntity<?> dislikePost(@PathVariable String postId) {

        LikeDto likeDto = likeService.dislikePost(postId);

        // Get the post author's user (assuming postService can provide it)
        UserDto postAuthor = postService.getPostById(postId).getUser();

        // Create a notification
        notificationService.createNotification(postAuthor, "A new dislike on your post.");

        //String successMessage = "You disliked post with ID: " + postId + " successfully.";
        return new ResponseEntity<>(likeDto, HttpStatus.CREATED);
    }

    @PostMapping("/comments/{commentId}/like")
    public ResponseEntity<?> likeComment(@PathVariable String commentId) {
        LikeDto likeDto = likeService.likeComment(commentId);

        // Get the comments author's user (assuming postService can provide it)
        //UserDto commentAuthor = commentService.getPostById(postId).getUser();

        // Create a notification
        //notificationService.createNotification(postAuthor, "A new dislike on your post.");

        return new ResponseEntity<>(likeDto, HttpStatus.CREATED);
    }

    @PostMapping("/comments/{commentId}/dislike")
    public ResponseEntity<?> dislikeComment(@PathVariable String commentId) {
        LikeDto likeDto = likeService.dislikeComment(commentId);

        // Get the comments author's user (assuming postService can provide it)
        //UserDto commentAuthor = commentService.getPostById(postId).getUser();

        // Create a notification
        //notificationService.createNotification(postAuthor, "A new dislike on your post.");

        return new ResponseEntity<>(likeDto, HttpStatus.CREATED);
    }

    @GetMapping("/posts/{postId}/likes")
    public ResponseEntity<Integer> countLikesForPost(@PathVariable String postId) {
        int likes = likeService.countLikesForPost(postId);

        return new ResponseEntity<>(likes, HttpStatus.OK);
    }

    @GetMapping("/posts/{postId}/dislikes")
    public ResponseEntity<Integer> countDislikesForPost(@PathVariable String postId) {
        int dislikes = likeService.countDislikesForPost(postId);
        return new ResponseEntity<>(dislikes, HttpStatus.OK);
    }

    @GetMapping("/comments/{commentId}/likes")
    public ResponseEntity<Integer> countLikesForComment(@PathVariable String commentId) {
        int likes = likeService.countLikesForComment(commentId);
        return new ResponseEntity<>(likes, HttpStatus.OK);
    }

    @GetMapping("/comments/{commentId}/dislikes")
    public ResponseEntity<Integer> countDislikesForComment(@PathVariable String commentId) {
        int dislikes = likeService.countDislikesForComment(commentId);
        return new ResponseEntity<>(dislikes, HttpStatus.OK);
    }

}

