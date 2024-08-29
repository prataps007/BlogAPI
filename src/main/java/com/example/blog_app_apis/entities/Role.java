package com.example.blog_app_apis.entities;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.HashSet;
import java.util.Set;

@Document(collection = "role")
@Data
public class Role {

    @Id
    private String id;

    private String name;

   // private Set<String> permissions = new HashSet<>();

}
