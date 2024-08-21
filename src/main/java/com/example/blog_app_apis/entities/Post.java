package com.example.blog_app_apis.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.*;

@Document(collection = "posts")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Post {

    @Id
    private String postId; // Using String for MongoDB ObjectId

    @Field("post_title")
    private String title;

    @Field("content")
    private String content;

    private String imageName;

    private Date addedDate;

    // Storing references to Category and User Object
    @DBRef
    private Category category;

    @DBRef
    private User user;


//    // Storing references to comments
    @DBRef
    private List<Comment> comments = new ArrayList<>();


}
