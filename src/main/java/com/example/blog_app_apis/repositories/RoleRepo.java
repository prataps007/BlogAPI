package com.example.blog_app_apis.repositories;

import com.example.blog_app_apis.entities.Role;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepo extends MongoRepository<Role, String> {

    Role findByName(String name);
}
