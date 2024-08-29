package com.example.blog_app_apis.entities;

import com.example.blog_app_apis.payloads.UserDto;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Document(collection = "comments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Comment {

    @Id
    private String id;  // Using String for MongoDB ObjectId

    private String content;

    @DBRef
    private Post post;

    // Store the user who created the comment
    @DBRef
    private User user;  ; // Reference to the user who created the comment

    private Set<Like> likes = new HashSet<>();

}

