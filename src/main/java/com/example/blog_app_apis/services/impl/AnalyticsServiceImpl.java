package com.example.blog_app_apis.services.impl;

import com.example.blog_app_apis.entities.Activity;
import com.example.blog_app_apis.payloads.PopularPostDto;
import com.example.blog_app_apis.payloads.UserEngagementDto;
import com.example.blog_app_apis.repositories.ActivityRepo;
import com.example.blog_app_apis.services.AnalyticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class AnalyticsServiceImpl implements AnalyticsService {

    @Autowired
    private ActivityRepo userActivityRepository;

    public List<UserEngagementDto> getUserEngagement() {
        // Step 1: Retrieve all user activities
        List<Activity> allActivities = userActivityRepository.findAll();

        // Step 2: Group the activities by user ID
        Map<String, List<Activity>> activitiesGroupedByUser = allActivities.stream()
                .collect(Collectors.groupingBy(Activity::getUserId));

        // Step 3: Create a list to hold the user engagement data
        List<UserEngagementDto> userEngagementList = new ArrayList<>();

        // Step 4: Iterate over each entry in the grouped activities
        for (Map.Entry<String, List<Activity>> entry : activitiesGroupedByUser.entrySet()) {
            String userId = entry.getKey(); // Get the user ID
            List<Activity> userActivities = entry.getValue();

            // Count the number of activities for this user
            int activityCount = userActivities.size();

            // Step 5: Count the types of activities performed by the user
            Map<String, Integer> activityTypeCount = userActivities.stream()
                    .collect(Collectors.groupingBy(Activity::getAction, Collectors.summingInt(e -> 1)));

            // Step 6: Create a UserEngagementDto object for this user
            UserEngagementDto userEngagementDto = new UserEngagementDto(userId, activityCount, activityTypeCount);

            // Step 7: Add the UserEngagementDto object to the list
            userEngagementList.add(userEngagementDto);
        }

        // Step 8: Return the list of user engagement data
        return userEngagementList;
    }


    public List<PopularPostDto> getPopularPosts() {
        // Logic to calculate popular posts based on user activities

        // Step 1: Retrieve all activities related to liking posts
        List<Activity> likeActivities = userActivityRepository.findByAction("LIKE_POST");

        // Step 2: Group the like activities by the target post ID
        Map<String, List<Activity>> activitiesGroupedByPost = likeActivities.stream()
                .collect(Collectors.groupingBy(Activity::getTargetId));

        // Step 3: Create a list to hold the popular post data
        List<PopularPostDto> popularPostList = new ArrayList<>();

        // Step 4: Iterate over each entry in the grouped activities
        for (Map.Entry<String, List<Activity>> entry : activitiesGroupedByPost.entrySet()) {
            String postId = entry.getKey(); // Get the post ID
            int likeCount = entry.getValue().size(); // Count the number of likes for this post

            // Step 5: Create a PopularPostDto object for this post
            PopularPostDto popularPostDto = new PopularPostDto(postId, likeCount);

            // Step 6: Add the PopularPostDto object to the list
            popularPostList.add(popularPostDto);
        }

        // Step 7: Sort the list in decreasing order of likes
        popularPostList.sort((p1, p2) -> Integer.compare(p2.getLikeCount(), p1.getLikeCount()));

        // Step 8: Return the sorted list of popular posts
        return popularPostList;

    }
}

