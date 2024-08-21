package com.example.blog_app_apis.services;

import com.example.blog_app_apis.entities.User;
import com.example.blog_app_apis.payloads.UserDto;

import java.util.List;

public interface UserService {

    UserDto createUser(UserDto user);
    UserDto updateUser(UserDto user, String userId);
    UserDto getUserById(String userId);
    List<UserDto> getAllUsers();

    void deleteUser(String userId);

    User assignRoleToUser(String userId, String roleName);
}
