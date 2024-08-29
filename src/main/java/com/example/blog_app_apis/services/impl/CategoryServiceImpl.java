package com.example.blog_app_apis.services.impl;

import com.example.blog_app_apis.entities.Category;
import com.example.blog_app_apis.exceptions.ResourceNotFoundException;
import com.example.blog_app_apis.payloads.CategoryDto;
import com.example.blog_app_apis.repositories.CategoryRepo;
import com.example.blog_app_apis.services.CategoryService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryRepo categoryRepo;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public CategoryDto createCategory(CategoryDto categoryDto) {

        if (categoryDto.getCategoryTitle() == null || categoryDto.getCategoryTitle().isEmpty()) {
            throw new IllegalArgumentException("Category title cannot be empty");
        }

        Category cat = modelMapper.map(categoryDto, Category.class);

        System.out.println("Creating category: " + categoryDto.getCategoryTitle());

        Category addedCat = categoryRepo.save(cat);
        return this.modelMapper.map(addedCat, CategoryDto.class);
    }

    @Override
    public CategoryDto updateCategory(CategoryDto categoryDto, String categoryId) {
        Category cat = categoryRepo.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "Category ID", categoryId));

        cat.setCategoryTitle(categoryDto.getCategoryTitle());
        cat.setCategoryDescription(categoryDto.getCategoryDescription());

        Category updatedCat = categoryRepo.save(cat);
        return this.modelMapper.map(updatedCat, CategoryDto.class);
    }

    @Override
    public void deleteCategory(String categoryId) {
        Category cat = categoryRepo.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "Category ID", categoryId));
        this.categoryRepo.delete(cat);
    }

    // get category by id
    @Override
    public CategoryDto getCategory(String categoryId) {
        Category cat = categoryRepo.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "Category ID", categoryId));
        return this.modelMapper.map(cat, CategoryDto.class);
    }

    // get all categories
    @Override
    public List<CategoryDto> getCategories() {
        List<Category> categories = categoryRepo.findAll();
        return categories.stream()
                .map(cat -> this.modelMapper.map(cat, CategoryDto.class))
                .collect(Collectors.toList());
    }

    // get all posts of a category  -- implement this service
}
