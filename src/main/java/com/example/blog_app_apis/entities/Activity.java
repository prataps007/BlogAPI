package com.example.blog_app_apis.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;

@Document(collection = "activities")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Activity {

    @Id
    private String id;

    private String userId; // User who performed the action

    private String action;   // type of action - create , update , delete, like , dislike, comment

    private String type; // Type of activity (POST, COMMENT, LIKE)

    private String targetId; // ID of the post or comment that is the target of the activity

    private LocalDateTime timestamp;

    // Constructors, getters, setters
}
