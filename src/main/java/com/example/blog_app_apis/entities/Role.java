package com.example.blog_app_apis.entities;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "role")
@Data
public class Role {

    @Id
    private String id;

    private String name;

}
