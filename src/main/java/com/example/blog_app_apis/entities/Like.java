package com.example.blog_app_apis.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "likes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Like {

    @Id
    private String id;

    private String userId;  // User who liked/disliked

    private String postId;  // Optional: post associated with the like/dislike

    private String commentId; // Optional: comment associated with the like/dislike

    private boolean like;  // true for like, false for dislike
}
