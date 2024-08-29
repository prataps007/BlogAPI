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
public class LikeDto {

    @Id
    private String id;

    private String userId;

    private String postId;

    private String commentId;

    private boolean like;

}
