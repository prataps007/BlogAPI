package com.example.blog_app_apis.payloads;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.DBRef;

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

    private Date addedDate;

    private CategoryDto category;

    private UserDto user;

    private List<CommentDto> comments = new ArrayList<>();
}
