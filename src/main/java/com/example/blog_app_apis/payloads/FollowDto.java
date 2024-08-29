package com.example.blog_app_apis.payloads;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FollowDto {

    @Id
    private String id;

    private String followerId;  // The user who follows

    private String followedId;  // The user who is followed
}
