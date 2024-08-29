package com.example.blog_app_apis.controllers;


import com.example.blog_app_apis.payloads.ApiResponse;
import com.example.blog_app_apis.payloads.CategoryDto;
import com.example.blog_app_apis.services.CategoryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {


    @Autowired
    private CategoryService categoryService;

    // create
    @PreAuthorize("hasRole('ROLE_ADMIN')")  // only admin can create category
    @PostMapping("/")
    public ResponseEntity<CategoryDto> createCategory(@Valid @RequestBody CategoryDto categoryDto){
        CategoryDto createCategory = categoryService.createCategory(categoryDto);
        return new ResponseEntity<CategoryDto>(createCategory, HttpStatus.CREATED);
    }

    // update
    @PreAuthorize("hasRole('ROLE_ADMIN')")  // only admin can update category
    @PutMapping("/{categoryId}")
    public ResponseEntity<CategoryDto> updateCategory(@Valid @RequestBody CategoryDto categoryDto, @PathVariable String categoryId){
        CategoryDto updatedCategory = categoryService.updateCategory(categoryDto,categoryId);
        return new ResponseEntity<CategoryDto>(updatedCategory,HttpStatus.OK);
    }

    // delete
    @PreAuthorize("hasRole('ROLE_ADMIN')")  // only admin can delete category
    @DeleteMapping("/{categoryId}")
    public ResponseEntity<ApiResponse> deleteCategory(@PathVariable String categoryId){
        categoryService.deleteCategory(categoryId);

        return new ResponseEntity<ApiResponse>(new ApiResponse("category is deleted successfully !!",true),HttpStatus.OK);
    }

    // get
    @GetMapping("/{categoryId}")
    public ResponseEntity<CategoryDto> getCategory(@PathVariable String categoryId){
        CategoryDto categoryDto = categoryService.getCategory(categoryId);

        return new ResponseEntity<CategoryDto>(categoryDto,HttpStatus.OK);
    }

    // get all
    @GetMapping("/")
    public ResponseEntity<List<CategoryDto>> getCategories(){
        List<CategoryDto> categories = categoryService.getCategories();
        return ResponseEntity.ok(categories);
    }
}
