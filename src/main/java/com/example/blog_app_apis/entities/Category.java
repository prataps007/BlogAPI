package com.example.blog_app_apis.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.ArrayList;
import java.util.List;

@Document(collection = "categories")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Category {

    @Id
    private String categoryId;  // Use String for MongoDB ObjectId

    @Field("title")  // Use @Field to map fields if needed
    private String categoryTitle;

    @Field("description")
    private String categoryDescription;

    // Embedding posts within the Category document or storing references
    @DBRef
    private List<Post> posts = new ArrayList<>();
}
