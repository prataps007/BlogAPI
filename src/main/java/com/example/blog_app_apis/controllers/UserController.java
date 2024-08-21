package com.example.blog_app_apis.controllers;

import com.example.blog_app_apis.entities.User;
import com.example.blog_app_apis.payloads.ApiResponse;
import com.example.blog_app_apis.payloads.UserDto;
import com.example.blog_app_apis.services.UserService;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;


@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    // POST - CREATE USER
    @PostMapping("/")
    public ResponseEntity<UserDto> createUser(@Valid @RequestBody UserDto userDto){
        UserDto createUserDto = userService.createUser(userDto);

        return new ResponseEntity<>(createUserDto, HttpStatus.CREATED);
    }

    // PUT - UPDATE USER
    @PutMapping("/{userId}")
    public ResponseEntity<UserDto> updateUser(@Valid @RequestBody UserDto userDto,@PathVariable String userId){
        UserDto updatedUser = userService.updateUser(userDto,userId);

        return new ResponseEntity<>(updatedUser,HttpStatus.OK);
    }

    // DELETE - DELETE USER
    @PreAuthorize("hasRole('ADMIN')")  // only admin can perform this operation
    @DeleteMapping("/{userId}")
    public ResponseEntity<ApiResponse> deleteUser(@PathVariable String userId){
        userService.deleteUser(userId);

        return ResponseEntity.ok(new ApiResponse("User deleted successfully", true));
    }

    // GET - USER GET
    @GetMapping("/")
    public ResponseEntity<List<UserDto>> getAllUsers(){
        List<UserDto> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserDto> getUserById(@PathVariable String userId){
        UserDto user = userService.getUserById(userId);
        return ResponseEntity.ok(user);
    }

    // Post - assign role to a user
    @PostMapping("/{userId}/role/{roleName}")
    public User assignRoleToUser(@PathVariable String userId, @PathVariable String roleName) {
        return userService.assignRoleToUser(userId, roleName);
    }
}
