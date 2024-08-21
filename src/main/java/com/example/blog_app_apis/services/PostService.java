package com.example.blog_app_apis.services;

import com.example.blog_app_apis.entities.Post;
import com.example.blog_app_apis.payloads.PostDto;
import com.example.blog_app_apis.payloads.PostResponse;
import org.springframework.stereotype.Service;

import java.util.List;


public interface PostService {

    // create
    PostDto createPost(PostDto postDto, String userId, String categoryId);  // String userId, String categoryId

    // update
    PostDto updatePost(PostDto postDto,String postId);

    // delete
    void deletePost(String postId);

    // get all post
    PostResponse getAllPost(Integer pageNumber, Integer pageSize, String sortBy, String sortDir);

    // get single post
    PostDto getPostById(String postId);

    // get all post by category
    List<PostDto> getPostsByCategory(String categoryId);

    // get all post by user
    List<PostDto> getPostsByUser(String userId);

    // search posts
    List<PostDto> searchPosts(String keyword);
}
