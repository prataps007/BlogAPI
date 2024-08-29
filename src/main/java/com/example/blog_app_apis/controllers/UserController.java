package com.example.blog_app_apis.controllers;

import com.example.blog_app_apis.config.AppConstants;
import com.example.blog_app_apis.payloads.ApiResponse;
import com.example.blog_app_apis.payloads.PaginatedApiResponse;
import com.example.blog_app_apis.payloads.UserDto;
import com.example.blog_app_apis.services.UserService;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


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
    // only the same user can update his own profile
    @PreAuthorize("@userServiceImpl.isProfileOwner(#userId)")
    @PutMapping("/{userId}")
    public ResponseEntity<UserDto> updateUser(@Valid @RequestBody UserDto userDto,@PathVariable String userId){
        UserDto updatedUser = userService.updateUser(userDto,userId);

        return new ResponseEntity<>(updatedUser,HttpStatus.OK);
    }

    // DELETE - DELETE USER
    @PreAuthorize("hasRole('ROLE_ADMIN') or @userServiceImpl.isProfileOwner(#userId)")  // only admin or the same user can delete user
    @DeleteMapping("/{userId}")
    public ResponseEntity<ApiResponse> deleteUser(@PathVariable String userId){
        userService.deleteUser(userId);

        return ResponseEntity.ok(new ApiResponse("User deleted successfully", true));
    }

    // GET - USER GET
    @GetMapping("/")
    public ResponseEntity<PaginatedApiResponse> getAllUsers(@RequestParam(value="pageNumber",defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
                                                            @RequestParam(value = "pageSize",defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
                                                            @RequestParam(value="sortBy",defaultValue = AppConstants.SORT_BY, required = false) String sortBy,
                                                            @RequestParam(value = "sortDir",defaultValue = AppConstants.SORT_DIR, required = false) String sortDir){

        //List<UserDto> users = userService.getAllUsers(pageNumber,pageSize,sortBy,sortDir);

        PaginatedApiResponse users = userService.getAllUsers(pageNumber,pageSize,sortBy,sortDir);

        return ResponseEntity.ok(users);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserDto> getUserById(@PathVariable String userId){
        UserDto user = userService.getUserById(userId);
        return ResponseEntity.ok(user);
    }

    // Post - assign role to a user
    @PreAuthorize("hasRole('ROLE_ADMIN')")  // only admin can perform this operation
    @PostMapping("/{userId}/role/{roleName}")
    public UserDto assignRoleToUser(@PathVariable String userId, @PathVariable String roleName) {
        return userService.assignRoleToUser(userId, roleName);
    }
}
