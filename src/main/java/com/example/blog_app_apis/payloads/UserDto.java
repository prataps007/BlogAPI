package com.example.blog_app_apis.payloads;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@NoArgsConstructor
@Getter
@Setter
public class UserDto {

    //private int id;
    private String id; // Changed to String to match MongoDB ObjectId

    @NotBlank(message="Name must not be blank")
    @Size(min=3, message="Name must be at least 3 characters long")
    private String name;

    @NotEmpty(message = "Email cannot be empty")
    @Email(message = "Email address is not valid")
    private String email;

    @NotEmpty(message = "Password cannot be empty")
    @Size(min = 3, message = "Password should have at least 3 characters")
    private String password;

    @NotEmpty(message = "About cannot be empty")
    private String about;
}
