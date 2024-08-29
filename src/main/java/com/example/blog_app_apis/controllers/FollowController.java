package com.example.blog_app_apis.controllers;

import com.example.blog_app_apis.exceptions.ResourceNotFoundException;
import com.example.blog_app_apis.payloads.ApiResponse;
import com.example.blog_app_apis.payloads.FollowDto;
import com.example.blog_app_apis.repositories.UserRepo;
import com.example.blog_app_apis.services.FollowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/follows")
public class FollowController {

    @Autowired
    private FollowService followService;

    @Autowired
    private UserRepo userRepo;

    @PostMapping("/{userId}/follow")
    public ResponseEntity<?> followUser(@PathVariable String userId) {
        // Check if followed user exists
        if (!userRepo.existsById(userId)) {
            return new ResponseEntity<>(new ApiResponse("User not found.", false), HttpStatus.NOT_FOUND);
        }

        try {
            FollowDto followDto = followService.followUser(userId);
            return new ResponseEntity<>(followDto, HttpStatus.OK);
        } catch (IllegalStateException e) {
            return new ResponseEntity<>(new ApiResponse(e.getMessage(), false), HttpStatus.CONFLICT);
        }
    }

    @DeleteMapping("/{userId}/unfollow")
    public ResponseEntity<?> unfollowUser(@PathVariable String userId) {
        // Check if followed user exists
        if (!userRepo.existsById(userId)) {
            return new ResponseEntity<>(new ApiResponse("User not found.", false), HttpStatus.NOT_FOUND);
        }

        try {
            FollowDto followDto = followService.unfollowUser(userId);
            return new ResponseEntity<>(followDto, HttpStatus.OK);
        } catch (IllegalStateException e) {
            return new ResponseEntity<>(new ApiResponse(e.getMessage(), false), HttpStatus.CONFLICT);
        }
    }

    @GetMapping("/{userId}/followers/count")
    public ResponseEntity<Long> countFollowers(@PathVariable String userId) {
        long count = followService.countFollowers(userId);
        return new ResponseEntity<>(count, HttpStatus.OK);
    }
}
