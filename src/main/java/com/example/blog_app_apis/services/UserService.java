package com.example.blog_app_apis.services;

import com.example.blog_app_apis.payloads.PaginatedApiResponse;
import com.example.blog_app_apis.payloads.UserDto;

public interface UserService {

    UserDto createUser(UserDto user);
    UserDto updateUser(UserDto user, String userId);
    UserDto getUserById(String userId);
    PaginatedApiResponse getAllUsers(Integer pageNumber, Integer pageSize, String sortBy, String sortDir);
    void deleteUser(String userId);
    UserDto assignRoleToUser(String userId, String roleName);
}
