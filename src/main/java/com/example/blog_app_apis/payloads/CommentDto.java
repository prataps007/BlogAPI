package com.example.blog_app_apis.payloads;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
public class CommentDto {

    private String id; // Changed to String to match MongoDB ObjectId
    private String content;
}
