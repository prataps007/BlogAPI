package com.example.blog_app_apis.services.impl;

import com.example.blog_app_apis.config.AppConstants;
import com.example.blog_app_apis.entities.Role;
import com.example.blog_app_apis.entities.User;
import com.example.blog_app_apis.exceptions.ResourceNotFoundException;
import com.example.blog_app_apis.payloads.PaginatedApiResponse;
import com.example.blog_app_apis.payloads.UserDto;
import com.example.blog_app_apis.repositories.RoleRepo;
import com.example.blog_app_apis.repositories.UserRepo;
import com.example.blog_app_apis.services.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private RoleRepo roleRepo;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    // Convert User entity to UserDto
    private UserDto userToDto(User user) {
        UserDto userDto = modelMapper.map(user, UserDto.class);
        return userDto;
    }

    // Convert UserDto to User entity
    private User dtoToUser(UserDto userDto) {
        User user = modelMapper.map(userDto,User.class);
        return user;
    }

    @Override
    public UserDto createUser(UserDto userDto) {
        User user = dtoToUser(userDto);

        // Encode the user's password with BCrypt
        System.out.println("Encoding the password of user : " + user.getName());
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        User savedUser = userRepo.save(user);
        savedUser.setPosts(new ArrayList<>());
        return this.userToDto(savedUser);
    }

    @Override
    public UserDto updateUser(UserDto userDto, String userId) {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User","id",userId));

        user.setName(userDto.getName());
        user.setEmail(userDto.getEmail());
        user.setPassword(userDto.getPassword());
        user.setAbout(userDto.getAbout());

        User updatedUser = this.userRepo.save(user);
        return userToDto(updatedUser);
    }

    @Override
    public UserDto getUserById(String userId) {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User","Id",userId));

        return this.userToDto(user);
    }

    @Override
    public PaginatedApiResponse getAllUsers(Integer pageNumber, Integer pageSize, String sortBy, String sortDir) {

        Sort sort=null;
        if(sortDir.equalsIgnoreCase(AppConstants.SORT_DIR)) {
            sort=Sort.by(sortBy).ascending();
        }
        else{
            sort = Sort.by(sortBy).descending();
        }

        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);


        Page<User> pageUser = userRepo.findAll(pageable);
        List<User> allUsers = pageUser.getContent();
        List<UserDto> userDtos = allUsers.stream().map((user) -> modelMapper.map(user,UserDto.class)).collect(Collectors.toList());

//        List<User> users = userRepo.findAll();
//        List<UserDto> userDtos = users.stream().map(this::userToDto).toList();

        PaginatedApiResponse paginatedApiResponse = new PaginatedApiResponse();
        paginatedApiResponse.setContent(userDtos);
        paginatedApiResponse.setPageNumber(pageUser.getNumber());
        paginatedApiResponse.setPageSize(pageUser.getSize());
        paginatedApiResponse.setTotalElements(pageUser.getTotalElements());
        paginatedApiResponse.setTotalPages(pageUser.getTotalPages());
        paginatedApiResponse.setLastPage(pageUser.isLast());

        //return userDtos;
        return paginatedApiResponse;
    }

    @Override
    public void deleteUser(String userId) {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User","Id",userId));

        this.userRepo.delete(user);
    }

    // assign role to user - only admin has authority to perform this operation

    public UserDto assignRoleToUser(String userId, String roleName) {
        User user = userRepo.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User","Id",userId));
        Role role = roleRepo.findByName(roleName);
        if (role == null) {
            throw new ResourceNotFoundException("Role", "name", roleName);
        }
        // avoid duplicate role casting
        if (!user.getRoles().contains(role)) {
            user.getRoles().add(role);
            userRepo.save(user);
        }

        // Debug log to check the roles assigned to the user
        System.out.println("Roles assigned to user: " + user.getRoles());

        UserDto userDto = userToDto(user);

        return userDto;
    }

    // This method will check if the authenticated user is the owner of the profile being updated:
    public boolean isProfileOwner(String userId) {
        // Get the current authenticated user
        User currentUser = getCurrentUser();

        // Check if the current user's ID matches the ID of the user whose profile is being updated
        return currentUser.getId().equals(userId);
    }

    private User getCurrentUser() {
        // Extract the username from the security context
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        // Fetch the user from the database using the username
        return userRepo.findByEmail(username)
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", username));
    }
}
