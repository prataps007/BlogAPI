package com.example.blog_app_apis.repositories;

import com.example.blog_app_apis.entities.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface UserRepo extends MongoRepository<User,String> {

    Optional<User> findByEmail(String email);
}
