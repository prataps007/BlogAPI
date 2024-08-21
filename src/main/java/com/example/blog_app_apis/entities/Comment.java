package com.example.blog_app_apis.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "comments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Comment {

    @Id
    private String id;  // Using String for MongoDB ObjectId

    private String content;

    // Store the user who created the comment -- complete this part
    // private UserDto user;  // Store user information or just userId

    // Storing the ID of the post this comment belongs to
    //private String postId;  // Reference to Post's ObjectId

    @DBRef
    private Post post;
}

