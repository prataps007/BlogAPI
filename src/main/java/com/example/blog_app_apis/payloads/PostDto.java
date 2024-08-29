package com.example.blog_app_apis.payloads;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.*;

@Getter
@Setter
@NoArgsConstructor
public class PostDto {

    //private Integer postId;
    private String postId; // Changed to String to match MongoDB ObjectId

    private String title;

    private String content;

    private String imageName;

    private boolean isDraft;
    private Date scheduledPublishTime;

    private Date addedDate;

    private CategoryDto category;

    private UserDto user;
    //private String user;

    //private List<CommentDto> comments = new ArrayList<>();

}
