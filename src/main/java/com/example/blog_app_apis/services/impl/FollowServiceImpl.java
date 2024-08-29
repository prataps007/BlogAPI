package com.example.blog_app_apis.services.impl;

import com.example.blog_app_apis.entities.Activity;
import com.example.blog_app_apis.entities.Follow;
import com.example.blog_app_apis.entities.User;
import com.example.blog_app_apis.exceptions.ResourceNotFoundException;
import com.example.blog_app_apis.payloads.FollowDto;
import com.example.blog_app_apis.repositories.ActivityRepo;
import com.example.blog_app_apis.repositories.FollowRepo;
import com.example.blog_app_apis.repositories.UserRepo;
import com.example.blog_app_apis.services.FollowService;
import com.example.blog_app_apis.services.SecurityService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Date;

@Service
public class FollowServiceImpl implements FollowService {

    @Autowired
    private FollowRepo followRepository;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private SecurityService securityService; // Inject the SecurityService

    @Autowired
    private ActivityRepo activityRepo;


    @Autowired
    private ModelMapper modelMapper;

    @Override
    public FollowDto followUser(String followedId) {
        User currUser = securityService.getCurrentUser(); // Move here to ensure correct security context
        String followerId = currUser.getId();

        if (!followRepository.existsByFollowerIdAndFollowedId(followerId, followedId)) {  // If not already followed only then follow
            Follow follow = new Follow(null, followerId, followedId);
            followRepository.save(follow);

            // Update the followingList of the current user
            currUser.getFollowingList().add(followedId);
            userRepo.save(currUser);

            // Update the followersList of the followed user
            User followedUser = userRepo.findById(followedId)
                    .orElseThrow(() -> new ResourceNotFoundException("User", "id", followedId));
            followedUser.getFollowersList().add(followerId);
            userRepo.save(followedUser);

            // Create an activity record
            Activity activity = new Activity();
            activity.setUserId(followerId);
            activity.setAction("FOLLOW");
            activity.setType("FOLLOW");
            activity.setTargetId(follow.getId());
            activity.setTimestamp(LocalDateTime.now());

            activityRepo.save(activity);

            return modelMapper.map(follow, FollowDto.class);
        }

        // Handle already followed case with conflict
        throw new IllegalStateException("Conflict: User is already following this user.");
    }

    @Override
    public FollowDto unfollowUser(String followedId) {
        User currUser = securityService.getCurrentUser(); // Move here to ensure correct security context
        String followerId = currUser.getId();

        if (followRepository.existsByFollowerIdAndFollowedId(followerId, followedId)) {  // If followed only then unfollow
            followRepository.deleteByFollowerIdAndFollowedId(followerId, followedId);

            // Remove from the followingList of the current user
            currUser.getFollowingList().remove(followedId);
            userRepo.save(currUser);

            // Remove from the followersList of the followed user
            User followedUser = userRepo.findById(followedId)
                    .orElseThrow(() -> new ResourceNotFoundException("User", "id", followedId));
            followedUser.getFollowersList().remove(followerId);
            userRepo.save(followedUser);

            Follow follow = new Follow(null, followerId, followedId);  // Represent the unfollowed relationship

            // Create an activity record
            Activity activity = new Activity();
            activity.setUserId(followerId);
            activity.setAction("UNFOLLOW");
            activity.setType("FOLLOW");
            activity.setTargetId(follow.getId());
            activity.setTimestamp(LocalDateTime.now());

            activityRepo.save(activity);

            return modelMapper.map(follow, FollowDto.class);
        }

        // Handle not followed case with conflict
        throw new IllegalStateException("Conflict: User is not following this user.");
    }

    @Override
    public long countFollowers(String userId) {
        return followRepository.countByFollowedId(userId);
    }

}
