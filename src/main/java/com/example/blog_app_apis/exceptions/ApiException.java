package com.example.blog_app_apis.exceptions;

public class ApiException extends RuntimeException{

    public ApiException() {
        super();
    }

    public ApiException(String message) {
        super(message);
    }
}
