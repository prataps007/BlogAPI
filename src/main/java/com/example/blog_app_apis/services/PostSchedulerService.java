package com.example.blog_app_apis.services;


import com.example.blog_app_apis.entities.Post;
import com.example.blog_app_apis.repositories.PostRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Service
public class PostSchedulerService {

    private static final Logger logger = LoggerFactory.getLogger(PostSchedulerService.class);

    @Autowired
    private PostRepo postRepository;

    @Scheduled(fixedRate = 60000) // Run every minute
    public void publishScheduledPosts() {
        logger.info("Running scheduled task to publish posts at: {}", LocalDateTime.now());

        Date now = new Date();
        List<Post> postsToPublish = postRepository.findAllByIsDraftTrueAndScheduledPublishTimeBeforeAndAddedDateIsNull(now);

        for (Post post : postsToPublish) {
            logger.info("Publishing post with ID: {}", post.getPostId());
            post.setAddedDate(now);
            postRepository.save(post);
        }
    }
}

