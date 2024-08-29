package com.example.blog_app_apis.payloads;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserEngagementDto {

    private String userId;

    private int activityCount;

    private Map<String, Integer> activityTypeCount;

}

