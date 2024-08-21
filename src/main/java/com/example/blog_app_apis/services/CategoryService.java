package com.example.blog_app_apis.services;

import com.example.blog_app_apis.payloads.CategoryDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CategoryService {

    // create
    CategoryDto createCategory(CategoryDto categoryDto);

    // update
    CategoryDto updateCategory(CategoryDto categoryDto, String categoryId);

    // delete
    void deleteCategory(String categoryId);

    // get
    CategoryDto getCategory(String categoryId);

    // get all
    List<CategoryDto> getCategories();
}
